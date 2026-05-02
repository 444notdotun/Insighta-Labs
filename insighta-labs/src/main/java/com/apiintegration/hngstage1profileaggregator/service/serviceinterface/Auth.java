package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface Auth {
    AuthResponse authenticate(String code,String codeVerifier,String state);
    String getCliResponse(AuthResponse authResponse);
    ResponseEntity<?> getWebResponse(HttpServletResponse httpResponse, AuthResponse authResponse);
}
