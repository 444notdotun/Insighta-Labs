package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;

public interface Auth {
    AuthResponse authenticate(String code);
}
