package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;

public interface Auth {
    AuthResponse authenticate(String code,String codeVerifier);
    String getCliOutput(String state,String code,String codeVerifier);
}
