package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ApiResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;
import com.apiintegration.hngstage1profileaggregator.utils.Validator;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
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
    public ResponseEntity<Void> redirectToGithub(@RequestParam String codeChallenge,@RequestParam(required = false) String redirectUrl) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", githubAuth.getRedirectUrl(codeChallenge,redirectUrl))
                .build();
    }
    @GetMapping("/github/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> callback(
            @RequestParam String code,
            @RequestParam(required = false) String codeVerifier,
            @RequestParam(required = false) String state) {
        AuthResponse authResponse = authService.authenticate(code, codeVerifier);
        if (state != null && !state.isBlank()) {
            String redirectUrl = getCliOutput(state, authResponse);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        }
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Authentication successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        AuthResponse authResponse = refreshTokenService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (refreshToken != null) {
            refreshTokenService.revokeToken(refreshToken);
        }
        return ResponseEntity.ok(new ApiResponse<>(null, "Logged out successfully"));
    }

    private static @NonNull String getCliOutput(String state, AuthResponse authResponse) {
        return state + "?accessToken=" + authResponse.getAccessToken()
                + "&refreshToken=" + authResponse.getRefreshToken()
                + "&username=" + authResponse.getUsername()
                + "&userId=" + authResponse.getUserId();
    }
}

