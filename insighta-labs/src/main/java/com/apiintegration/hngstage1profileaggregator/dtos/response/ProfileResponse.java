package com.apiintegration.hngstage1profileaggregator.dtos.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.UUID;

@Data
public class ProfileResponse {
    private UUID id;
    private String name;
    private String gender;
    @JsonProperty("gender_probability")
    private float genderProbability;
    @JsonProperty("sample_size")
    private Integer sampleSize;
    private Integer age;
    @JsonProperty("age_group")
    private String ageGroup;
    @JsonProperty("country_id")
    private String countryId;
    @JsonProperty("country_probability")
    private float countryProbability;
    private String countryName;
    @CreationTimestamp
    @JsonProperty("created_at")
    private String createdAt;
}
