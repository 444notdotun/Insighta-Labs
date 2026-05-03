package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.ExchangeTokenRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ApiResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.GithubUrlResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Auth authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private OAuth githubAuth;

    @GetMapping("/github")
    public ResponseEntity<?> redirectToGithub(@RequestParam String codeChallenge,
                                                                           @RequestParam String redirectUrl,
                                                                           @RequestParam boolean isWeb) {
        String githubUrl = githubAuth.getRedirectUrl(codeChallenge, redirectUrl, isWeb);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", githubUrl)
                .body(new ApiResponse<>(new GithubUrlResponse(githubUrl), "Redirecting to GitHub for authentication"));
    }

    @GetMapping("/github/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam(required = false) String state) {
        String callBackResponse = authService.requestVerifierFromClient(code,  state);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", callBackResponse)
                .build();
    }

    @PostMapping("/github/exchange")
    public ResponseEntity<?> exchangeTokenForCodeVerifier(
            @RequestBody ExchangeTokenRequest exchangeToken,
            HttpServletResponse httpResponse) {
        AuthResponse authResponse = authService.authenticate(exchangeToken);
        if (authResponse.getWeb()) {
            refreshTokenService.setRefreshCookies(httpResponse, authResponse);
            return ResponseEntity.ok(new ApiResponse<>(
                    Map.of(
                            "username", authResponse.getUsername(),
                            "userId", authResponse.getUserId()
                    ),
                    "Authentication successful"
            ));
        }
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Authentication successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestBody(required = false) RefreshRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String refreshToken = refreshTokenService.extractRefreshToken(request, httpRequest);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, "No refresh token found"));
        }
        AuthResponse authResponse = refreshTokenService.rotateRefreshToken(refreshToken);
        boolean isWebRequest = refreshTokenService.isWebRequest(request);
        if (isWebRequest) {
            refreshTokenService.setRefreshCookies(httpResponse, authResponse);
        }
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Token refreshed successfully"));
    }


}