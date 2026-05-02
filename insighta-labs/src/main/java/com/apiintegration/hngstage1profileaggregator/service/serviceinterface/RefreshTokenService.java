package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.model.RefreshToken;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.dtos.request.RefreshRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {
        String generateRefreshToken(Users user);
        RefreshToken validateRefreshToken(String token);
    AuthResponse rotateRefreshToken(String token);

    String extractRefreshToken(RefreshRequest request, HttpServletRequest httpRequest);

    void setRefreshCookies(HttpServletResponse httpResponse, AuthResponse authResponse);

    boolean isWebRequest(RefreshRequest request);
}

