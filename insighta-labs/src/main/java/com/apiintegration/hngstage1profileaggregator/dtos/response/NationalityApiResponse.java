package com.apiintegration.hngstage1profileaggregator.dtos.response;

import com.apiintegration.hngstage1profileaggregator.data.model.Country;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NationalityApiResponse {

        private Integer count;

        private String name;

        private ArrayList<Country> country;

        public NationalityApiResponse() {
            this.country = new ArrayList<>();
        }


}
