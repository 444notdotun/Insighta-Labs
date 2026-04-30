package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

public interface OAuth {
    String getAuthUrl();
    String getRedirectUrl(String codeChallenge,String redirectUrl);
}
