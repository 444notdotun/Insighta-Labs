package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ApiResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;

import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;

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
    public ResponseEntity<Void> redirectToGithub(
            @RequestParam String codeChallenge,
            @RequestParam(required = false) String redirectUrl) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", githubAuth.getRedirectUrl(codeChallenge, redirectUrl))
                .build();
    }

    @GetMapping("/github/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam String codeVerifier,
            @RequestParam(required = false) String state) {
        if (state != null && !state.isBlank()) {
            String redirectUrl = authService.getCliOutput(state, code, codeVerifier);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        }
        AuthResponse authResponse = authService.authenticate(code, codeVerifier);
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Authentication successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        AuthResponse authResponse = refreshTokenService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Token refreshed successfully"));
    }




}