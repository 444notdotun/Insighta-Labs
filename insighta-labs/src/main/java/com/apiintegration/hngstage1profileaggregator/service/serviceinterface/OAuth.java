package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

public interface OAuth {
    String getAuthUrl();
    String getAuthorizationUrl(String codeChallenge);
}
