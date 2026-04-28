package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String userId;
}
