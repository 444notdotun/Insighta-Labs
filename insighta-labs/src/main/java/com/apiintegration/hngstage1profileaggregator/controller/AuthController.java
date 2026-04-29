package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.CallBackRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ApiResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.Auth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

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
    public ResponseEntity<Void> redirectToGithub(@RequestParam String codeChallenge) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", githubAuth.getAuthorizationUrl(codeChallenge))
                .build();
    }
    @GetMapping("/github/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> callback(
            @RequestParam String code,
            @RequestParam(required = false) String codeVerifier) {
        AuthResponse authResponse = authService.authenticate(code, codeVerifier);
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Authentication successful"));
    }

    @GetMapping("/pkce-test")
    public ResponseEntity<String> pkceTest() throws Exception {
        String verifier = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWX";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(verifier.getBytes(StandardCharsets.US_ASCII));
        String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        return ResponseEntity.ok("verifier: " + verifier + "\nchallenge: " + challenge);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        AuthResponse authResponse = refreshTokenService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(authResponse, "Token refreshed successfully"));
    }
}

