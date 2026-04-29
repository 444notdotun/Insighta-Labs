package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.dtos.response.NationalityApiResponse;

public interface NationalityApi {
NationalityApiResponse getNationality(String name);
}
