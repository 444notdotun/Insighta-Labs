package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallBackResponse {
    private String clientUrl;
    private Boolean Web;
}
