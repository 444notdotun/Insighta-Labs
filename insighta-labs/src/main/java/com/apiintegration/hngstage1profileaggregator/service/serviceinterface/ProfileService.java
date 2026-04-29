package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.model.Profile;
import com.apiintegration.hngstage1profileaggregator.dtos.request.GetProfilesRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ProfileResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ServiceResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.Summary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProfileService {
    ServiceResponse<ProfileResponse> createProfile(GetProfilesRequest getProfilesRequest);

    ProfileResponse getProfileById(UUID id);

    Page<Summary> getProfiles(String gender, String ageGroup, String countryId, Integer minAge, Integer maxAge, Float minGenderProbability, Float minCountyProbability, Pageable pageable);
    void deleteProfile(UUID id);
    Page<Summary> search(String note,Pageable pageable);
    byte[] exportCsv(String gender, String ageGroup, String countryId, Integer minAge, Integer maxAge, Pageable pageable);
}
