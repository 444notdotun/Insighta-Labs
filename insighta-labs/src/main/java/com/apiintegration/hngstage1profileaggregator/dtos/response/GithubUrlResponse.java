package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Data;

@Data
public class GithubUrlResponse {
    private String githubUrl;
    public GithubUrlResponse(String githubUrl) {
        this.githubUrl = githubUrl;
    }
}
