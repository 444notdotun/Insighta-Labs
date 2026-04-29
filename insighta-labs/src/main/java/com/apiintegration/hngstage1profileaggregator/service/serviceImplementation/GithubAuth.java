package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubAuth implements OAuth {
    @Value("${github.client.id}")
    private String clientId;
    @Override
    public String getAuthUrl() {
        return "https://github.com/login/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(String codeChallenge) {
        return String.format("https://github.com/login/oauth/authorize?client_id=%s&code_challenge=%s)",clientId,codeChallenge);
    }


}
