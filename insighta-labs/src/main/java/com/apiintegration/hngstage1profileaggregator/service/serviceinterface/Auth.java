package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.request.ExchangeTokenRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface Auth {
    AuthResponse authenticate(ExchangeTokenRequest exchangeTokenRequest);
    String getCliResponse(AuthResponse authResponse);
    ResponseEntity<?> getWebResponse( AuthResponse authResponse);

    String requestVerifierFromClient(String code, String state);
}
