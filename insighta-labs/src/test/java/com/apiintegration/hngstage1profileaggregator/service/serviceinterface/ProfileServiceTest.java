package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.repository.ProfileRepository;
import com.apiintegration.hngstage1profileaggregator.dtos.request.GetProfilesRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ProfileResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.ServiceResponse;
import com.apiintegration.hngstage1profileaggregator.dtos.response.Summary;
import com.apiintegration.hngstage1profileaggregator.exception.ProfileExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProfileServiceTest {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    GetProfilesRequest getProfilesRequest = new GetProfilesRequest();

    @Test
    void testThatGetProfileCanSave(){
        getProfilesRequest.setName("Adedotun");
        ServiceResponse<ProfileResponse> profileResponse = profileService.createProfile(getProfilesRequest);
        assertNotNull(profileResponse);
        assertTrue(profileRepository.existsById(profileResponse.getData().getId()));
        System.out.println(profileResponse);
    }
    @Test
    void testThatGetProfileReturnsTrueIfProfileExists(){
        getProfilesRequest.setName("tochi");
        profileService.createProfile(getProfilesRequest);
       ServiceResponse< ProfileResponse> profileResponse = profileService.createProfile(getProfilesRequest);
        assertNotNull(profileResponse);
        assertTrue(profileRepository.existsById(profileResponse.getData().getId()));
        ServiceResponse< ProfileResponse> profileResponse1= profileService.createProfile(getProfilesRequest);
        assertTrue(profileResponse1.isStatus());
    }

    @Test
    void testThatProfileCanBeGottenById(){
        getProfilesRequest.setName("Adedotun");
        profileService.createProfile(getProfilesRequest);
        ServiceResponse< ProfileResponse> profileResponse = profileService.createProfile(getProfilesRequest);
        assertNotNull(profileResponse);
        assertTrue(profileRepository.existsById(profileResponse.getData().getId()));
        ProfileResponse profileResponse1 = profileService.getProfileById(profileResponse.getData().getId());
        assertNotNull(profileResponse1);
        assertEquals(profileResponse.getData(),profileResponse1);
    }

    @Test
    void testThatProfileReturnsExceptionIfProfileDoesNotExist(){
        assertThrows(ProfileExistException.class,()->profileService.getProfileById(UUID.randomUUID()));
    }

    @Test
    void test(){
        Page<Summary>  summary = profileService.getProfiles( null,null,null,null,null,null,null,PageRequest.of(5,1));
        Pageable pageable = PageRequest.of(5,1);
        assertEquals(summary,profileService.getProfiles( null,null,null,null,null,null,null,pageable));
        summary.getContent()
                .forEach(summary1 -> System.out.println(summary1.getClass()) );
        System.out.println(pageable);
    }




}