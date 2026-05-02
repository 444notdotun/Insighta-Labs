package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.request.GithubRequest;

public interface OAuth {
    String getAuthUrl();
    String getRedirectUrl(GithubRequest githubRequest);
}
