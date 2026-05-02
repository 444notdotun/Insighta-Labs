package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.data.model.Roles;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.data.repository.UsersRepository;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.GithubResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.GithubUserResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.JwtService;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Slf4j
@Service
public class AuthImplementation implements Auth {

    @Autowired
    private OAuth githubAuth;
    @Autowired
    private HttpClient client;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Value("${CLIENT_ID}")
    private String clientId;
    @Value("${CLIENT_SECRET}")
    private String clientSecret;


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public AuthResponse authenticate(String accessCode, String codeVerifier,String state) {
        Claims verifiedState = validateState(state);
        GithubResponse githubResponse = exchangeCodeForToken(accessCode, verifiedState.get("codeVerifier", String.class));
        if (githubResponse.getAccessToken() == null) {
            throw new RuntimeException("GitHub token exchange failed — invalid code or verifier");
        }
        GithubUserResponse githubUserResponse = fetchGithubUserProfile(githubResponse.getAccessToken());
        if (githubUserResponse.getGithubId() == null) {
            throw new RuntimeException("Failed to fetch GitHub user profile");
        }
        Users user = usersRepository.findUsersByGithubId(githubUserResponse.getGithubId())
                .orElseGet(() -> {
                    Users newUser = modelMapper.map(githubUserResponse, Users.class);
                    newUser.setRole(Roles.ANALYST);
                    return usersRepository.save(newUser);
                });
        return createAuthResponse(user,verifiedState);
    }

    @Override
    public String getCliResponse(AuthResponse authResponse) {
            return authResponse.getRedirectUrl()
                    + "?accessToken=" + authResponse.getAccessToken()
                    + "&refreshToken=" + authResponse.getRefreshToken()
                    + "&username=" + authResponse.getUsername()
                    + "&userId=" + authResponse.getUserId();
    }

    @Override
    public ResponseEntity<?> getWebResponse(HttpServletResponse httpResponse, AuthResponse authResponse) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", authResponse.getRedirectUrl()
                        + "?username=" + authResponse.getUsername()
                        + "&userId=" + authResponse.getUserId()
                        + "&accessToken=" + authResponse.getAccessToken()
                        + "&refreshToken=" + authResponse.getRefreshToken())
                .build();
    }

    private GithubResponse exchangeCodeForToken(String accessCode, String codeVerifier) {
        String body = String.format(
                "client_id=%s&client_secret=%s&code=%s&code_verifier=%s",
                clientId, clientSecret, accessCode, codeVerifier
        );
        HttpRequest request = getHttpRequest(body, githubAuth.getAuthUrl());
        HttpResponse<String> response = getHttpResponseForAccessCodeResponseApi(request);
        System.out.println("GitHub response: " + response.body());
        return objectMapper.readValue(response.body(), GithubResponse.class);
    }

    private GithubUserResponse fetchGithubUserProfile(String accessToken) {
        HttpRequest request = getUserProfileRequest(accessToken);
        HttpResponse<String> response = getUserProfileApi(request);
        return objectMapper.readValue(response.body(), GithubUserResponse.class);
    }
    private Claims validateState(String state){
        try {
            return jwtService.ValidateStateToken(state);

        } catch (RuntimeException e) {
            throw new RuntimeException("State has been tampered"+e.getMessage());
        }
    }

    private AuthResponse createAuthResponse(Users user,Claims claims) {
       AuthResponse authResponse = modelMapper.map(user, AuthResponse.class);
        authResponse.setAccessToken(jwtService.generateToken(user));
        authResponse.setRefreshToken(refreshTokenService.generateRefreshToken(user));
        authResponse.setRedirectUrl(claims.get("redirectUrl", String.class));
        authResponse.setWeb((Boolean) claims.get("isWeb"));
        return authResponse;
    }

    private HttpResponse<String> getHttpResponseForAccessCodeResponseApi(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("GitHub token exchange request failed: " + e.getMessage());
        }
    }

    private HttpResponse<String> getUserProfileApi(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("GitHub user profile request failed: " + e.getMessage());
        }
    }

    private HttpRequest getUserProfileRequest(String accessToken) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .uri(URI.create("https://api.github.com/user"))
                .build();
    }

    private static HttpRequest getHttpRequest(String body, String url) {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .build();
    }
}