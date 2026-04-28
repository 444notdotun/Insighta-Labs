package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.data.model.GithubResponse;
import com.apiintegration.hngstage1profileaggregator.data.model.GithubUserResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AuthImplementation  implements Auth {
    @Autowired
    private OAuth githubAuth;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private HttpClient client;




    @Override
    public AuthResponse authenticate(String accessCode) {
        String body = getRequestBody(accessCode);
        String url = githubAuth.getAuthUrl();
        HttpResponse<String> response;
        HttpRequest request = getHttpRequest(body, url);
        response = getHttpResponseForAccessCodeResponseApi(request);
        GithubResponse githubResponse = objectMapper.readValue(response.body(), GithubResponse.class);
        request =getUserProfileRequest(githubResponse,request);
        response = getUserProfileApi(request);
        GithubUserResponse githubUserResponse = objectMapper.readValue(response.body(),GithubUserResponse.class);



    }

    private HttpResponse<String> getUserProfileApi(HttpRequest request) {
        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("user profile api threw error"+e.getMessage());
        }
        return response;
    }

    private HttpResponse<String> getHttpResponseForAccessCodeResponseApi(HttpRequest request) {
        HttpResponse<String> response;
        try{
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Api threw error"+e.getMessage());
        }
        return response;
    }

    private  HttpRequest getUserProfileRequest(GithubResponse githubResponse,HttpRequest request) {
        request = HttpRequest.newBuilder()
               .header("Authorization", "Bearer " + githubResponse.getAccessToken())
               .header("Accept", "application/json")
               .GET()
               .uri(URI.create("https://api.github.com/user"))
               .build();
        return request;
    }

    private  String getRequestBody(String accessCode) {
        return String.format("client_id=%s&client_secret=%s&code=%s",clientId,clientSecret, accessCode);
    }

    private static HttpRequest getHttpRequest(String body, String url) {
        return HttpRequest.newBuilder()
                .header("content-type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .build();
    }
}
