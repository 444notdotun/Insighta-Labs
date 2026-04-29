package com.apiintegration.hngstage1profileaggregator.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubResponse {
        @JsonProperty("access_token")
        private String accessToken;
        private String scope;
        @JsonProperty("token_type")
        private String tokenType;

}
