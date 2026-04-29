package com.apiintegration.hngstage1profileaggregator.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country {
        @JsonProperty("country_id")
        private String countryId;
        private float probability;
}
