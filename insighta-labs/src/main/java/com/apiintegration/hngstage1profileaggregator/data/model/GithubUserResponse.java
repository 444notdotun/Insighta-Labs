package com.apiintegration.hngstage1profileaggregator.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubUserResponse {
    @JsonProperty("id")
        private Integer githubId;
        @JsonProperty("login")
        private String userName;
        @JsonProperty("avatar_url")
        private String avatarUrl;
}
