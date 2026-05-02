package com.apiintegration.hngstage1profileaggregator.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String userId;
    private String redirectUrl;
    private Boolean web;
}
