package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import org.springframework.stereotype.Service;

@Service
public class GithubAuth implements OAuth {
    @Override
    public String getAuthUrl() {
        return "https://github.com/login/oauth/access_token";
    }
}
