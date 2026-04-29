package com.apiintegration.hngstage1profileaggregator.dtos.request;

import lombok.Data;

@Data
public class CallBackRequest {
    private String code;
    private String codeVerifier;
}
