package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.GithubRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ApiResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.GithubUrlResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Auth authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private OAuth githubAuth;

    @PostMapping("/github")
    public ResponseEntity<ApiResponse<GithubUrlResponse>> redirectToGithub(@RequestBody GithubRequest githubRequest) {
        String githubUrl = githubAuth.getRedirectUrl(githubRequest);
        return ResponseEntity.ok(new ApiResponse<>(new GithubUrlResponse(githubUrl), "Authorization URL generated"));
    }

    @GetMapping("/github/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam(required = false) String state) {
        AuthResponse authResponse = authService.authenticate(code, null, state);
        if (authResponse.getWeb()) {
            return authService.getWebResponse(authResponse);
        }
        String cliRedirect = authService.getCliResponse(authResponse);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", cliRedirect)
                .build();
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