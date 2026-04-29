package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.GenderizeApiResponse;

public interface GenderApi {
    GenderizeApiResponse getGender(String name);
}
