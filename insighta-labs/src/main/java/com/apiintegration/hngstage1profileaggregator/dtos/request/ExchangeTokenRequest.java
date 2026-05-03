package com.apiintegration.hngstage1profileaggregator.dtos.request;

import lombok.Data;

@Data
public class ExchangeTokenRequest {
    private String codeVerifier;
    private String exchangeToken;
}
