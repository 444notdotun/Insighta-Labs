package com.apiintegration.hngstage1profileaggregator.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class GetProfilesRequest {
    @Pattern (regexp = "[a-zA-Z\\s'-]+",message = "String is only allowed")
    @NotNull(message = "request can not be empty")
    private String name;
}
