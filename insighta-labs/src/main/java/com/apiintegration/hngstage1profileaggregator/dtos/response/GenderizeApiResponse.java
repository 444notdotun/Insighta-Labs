package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Data;

@Data
public class GenderizeApiResponse {

        private Integer count;

        private String name;

        private String gender;

        private float probability;

}
