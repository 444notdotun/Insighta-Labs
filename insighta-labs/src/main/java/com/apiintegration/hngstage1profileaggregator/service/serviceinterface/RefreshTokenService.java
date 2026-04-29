package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.model.RefreshToken;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;

public interface RefreshTokenService {
        String generateRefreshToken(Users user);
        RefreshToken validateRefreshToken(String token);
    AuthResponse rotateRefreshToken(String token);
    }

