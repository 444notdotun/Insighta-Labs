package com.apiintegration.hngstage1profileaggregator.utils;

import com.apiintegration.hngstage1profileaggregator.data.model.Profile;
import com.apiintegration.hngstage1profileaggregator.dtos.response.*;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Mapper {
    public static Profile mapApiResponsesToProfile(CompletableFuture<AgeApiResponse> ageApiResponseCompletableFuture, CompletableFuture<GenderizeApiResponse> genderizeApiResponseCompletableFuture, CompletableFuture<NationalityApiResponse> nationalizeApiCompletableFuture) {
        Profile profile = new Profile();
        profile.setName(nationalizeApiCompletableFuture.join().getName());
        profile.setGender(genderizeApiResponseCompletableFuture.join().getGender());
        profile.setGenderProbability(genderizeApiResponseCompletableFuture.join().getProbability());
        profile.setAge(ageApiResponseCompletableFuture.join().getAge());
        return profile;
    }

    public static ProfileResponse mapProfileToProfileResponse(Profile profile) {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setName(profile.getName());
        profileResponse.setGender(profile.getGender());
        profileResponse.setGenderProbability(profile.getGenderProbability());
        profileResponse.setAge(profile.getAge());
        profileResponse.setAgeGroup(profile.getAgeGroup());
        profileResponse.setCountryId(profile.getCountryId());
        profileResponse.setCountryProbability(profile.getCountryProbability());
        profileResponse.setId(profile.getId());
        profileResponse.setCountryName(profile.getCountryName());
        profileResponse.setCreatedAt(profile.getCreatedAt());
        return profileResponse;
    }

    public static ServiceResponse<ProfileResponse> mapProfileResponseToServiceResponse(ProfileResponse profileResponse, boolean success) {
        return new ServiceResponse<>(profileResponse,success);
    }

    public static Summary mapProfileToSummary(Profile profile) {
        Summary summary = new Summary();
        summary.setName(profile.getName());
        summary.setGender(profile.getGender());
        summary.setAge(profile.getAge());
        summary.setAgeGroup(profile.getAgeGroup());
        summary.setCountryId(profile.getCountryId());
        summary.setId(profile.getId());
       return summary;
    }
}
