package com.apiintegration.hngstage1profileaggregator.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubRequest {
    private String codeVerifier;
    private String codeChallenge;
    private String redirectUrl;
    @JsonProperty("isWeb")
    private boolean isWeb;
}
