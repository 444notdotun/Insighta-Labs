package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class AuthImplementation  implements Auth {
    @Autowired
    private OAuth githubAuth;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;

    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private HttpClient client;


    @Override
    public AuthResponse authenticate(String accessCode) {
        String body = getRequestBody(accessCode);
        String url = githubAuth.getAuthUrl();
        try{

        HttpRequest request = getHttpRequest(body, url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        GithubAuth githubAuth = objectMapper.readValue(response.body(), GithubAuth.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Api threw error"+e.getMessage());
        }



        return null;
    }

    private @NonNull String getRequestBody(String accessCode) {
        return String.format("""
               {
               "client_id":"%s",
               "client_secret":"%s",
               "code":"%s"
               """,clientId,clientSecret, accessCode);
    }

    private static HttpRequest getHttpRequest(String body, String url) {
        return HttpRequest.newBuilder()
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .build();
    }
}
