package com.apiintegration.hngstage1profileaggregator.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Summary {

    private UUID id;
    private String name;
    private String gender;
    private Integer age;
    @JsonProperty("age_group")
    private String ageGroup;
    @JsonProperty("country_id")
    private String countryId;
}
