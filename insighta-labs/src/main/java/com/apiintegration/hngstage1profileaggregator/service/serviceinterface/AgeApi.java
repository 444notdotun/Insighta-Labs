package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AgeApiResponse;

public interface AgeApi {
    AgeApiResponse getAge(String name);
}
