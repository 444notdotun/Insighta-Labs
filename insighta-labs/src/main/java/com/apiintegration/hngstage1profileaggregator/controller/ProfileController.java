package com.apiintegration.hngstage1profileaggregator.controller;

import com.apiintegration.hngstage1profileaggregator.dtos.request.GetProfilesRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.*;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.ProfileService;
import com.apiintegration.hngstage1profileaggregator.utils.SortFieldMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@CrossOrigin(origins = "*")

public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @GetMapping({"/","","/api"})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
    @GetMapping("/api/users/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<?> getMe(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse<>(Map.of(
                "userId", authentication.getName(),
                "role", authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        )));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @PostMapping("/api/profiles")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@Valid @RequestBody GetProfilesRequest getProfilesRequest) {
        ServiceResponse<ProfileResponse> profileResponseServiceResponse =profileService.createProfile(getProfilesRequest);
        if(profileResponseServiceResponse.isStatus()){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(profileResponseServiceResponse.getData(), "Profile already exist"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(profileResponseServiceResponse.getData()));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @GetMapping("/api/profiles/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileById(@NotNull(message = "id can not be empty") @PathVariable UUID id) {
        ProfileResponse profileResponse = profileService.getProfileById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(profileResponse));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @GetMapping("/api/profiles")
    public ResponseEntity<SpecificationApiResponse> getFilteredProfiles(@RequestParam(required = false)
                                                                            @Pattern(regexp = "(?i)(male|female)$", message = "gender can only be male or female")
                                                                            String gender,

                                                                        @RequestParam(name = "age_group", required = false)
                                                                            @Pattern(regexp = "(?i)(child|teenager|adult|senior)$", message = "age_group can only be child, teenager, adult, senior")
                                                                            String ageGroup,

                                                                        @RequestParam(name = "country_id", required = false)
                                                                            @Size(min = 2, max = 2, message = "country_id must be a 2-letter ISO code")
                                                                            String countryId,

                                                                        @RequestParam(name = "min_age", required = false)
                                                                            @Min(value = 0, message = "min_age must be a positive number")
                                                                            Integer minAge,

                                                                        @RequestParam(name = "max_age", required = false)
                                                                            @Min(value = 0, message = "max_age must be a positive number")
                                                                            Integer maxAge,

                                                                        @RequestParam(name = "min_gender_probability", required = false)
                                                                            @DecimalMin(value = "0.0", message = "min_gender_probability must be between 0 and 1")
                                                                            @DecimalMax(value = "1.0", message = "min_gender_probability must be between 0 and 1")
                                                                            Float minGenderProbability,

                                                                        @RequestParam(name = "min_country_probability", required = false)
                                                                            @DecimalMin(value = "0.0", message = "min_country_probability must be between 0 and 1")
                                                                            @DecimalMax(value = "1.0", message = "min_country_probability must be between 0 and 1")
                                                                            Float minCountryProbability,

                                                                        @RequestParam(name = "sort_by", required = false)
                                                                            @Pattern(regexp = "(?i)(age|created_at|gender_probability)$", message = "sort_by can only be age, created_at or gender_probability")
                                                                            String sortBy,

                                                                        @RequestParam(name = "order", required = false, defaultValue = "asc")
                                                                            @Pattern(regexp = "(?i)(asc|desc)$", message = "order can only be asc or desc")
                                                                            String order,

                                                                        @RequestParam(defaultValue = "1")
                                                                            @Min(value = 1, message = "page must be greater than 0")
                                                                            Integer page,

                                                                        @RequestParam(defaultValue = "10")
                                                                            @Min(value = 1, message = "limit must be at least 1")
                                                                            @Max(value = 50, message = "limit cannot exceed 50")
                                                                            Integer limit ){

        String entityField = SortFieldMapper.getSortField(sortBy);
        Sort sort = Sort.by(Sort.Direction.fromString(order),entityField);
        Pageable pageable = PageRequest.of(page-1,limit,sort);
        Page<Summary> profiles = profileService.getProfiles(gender,ageGroup,countryId,minAge,maxAge,minGenderProbability,minCountryProbability,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new SpecificationApiResponse(profiles));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/profiles/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> deleteProfile(@NotNull(message = "id can not be empty") @PathVariable UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(null));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @GetMapping("/api/profiles/search")
    public ResponseEntity<SpecificationApiResponse> search (@NotNull(message = "can not be null") @RequestParam String search){
        Pageable pageable = PageRequest.of(0,10);
        return ResponseEntity.status(HttpStatus.OK).body(new SpecificationApiResponse(profileService.search(search,pageable)));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @GetMapping("/api/profiles/export")
    public ResponseEntity<byte[]> exportCsv(@RequestParam(required = false)
                                                @Pattern(regexp = "(?i)(male|female)$", message = "gender can only be male or female")
                                                String gender,

                                            @RequestParam(name = "age_group", required = false)
                                                @Pattern(regexp = "(?i)(child|teenager|adult|senior)$", message = "age_group can only be child, teenager, adult, senior")
                                                String ageGroup,

                                            @RequestParam(name = "country_id", required = false)
                                                @Size(min = 2, max = 2, message = "country_id must be a 2-letter ISO code")
                                                String countryId,

                                            @RequestParam(name = "min_age", required = false)
                                                @Min(value = 0, message = "min_age must be a positive number")
                                                Integer minAge,

                                            @RequestParam(name = "max_age", required = false)
                                                @Min(value = 0, message = "max_age must be a positive number")
                                                Integer maxAge,

                                            @RequestParam(name = "min_gender_probability", required = false)
                                                @DecimalMin(value = "0.0", message = "min_gender_probability must be between 0 and 1")
                                                @DecimalMax(value = "1.0", message = "min_gender_probability must be between 0 and 1")
                                                Float minGenderProbability,

                                            @RequestParam(name = "min_country_probability", required = false)
                                                @DecimalMin(value = "0.0", message = "min_country_probability must be between 0 and 1")
                                                @DecimalMax(value = "1.0", message = "min_country_probability must be between 0 and 1")
                                                Float minCountryProbability,

                                            @RequestParam(name = "sort_by", required = false)
                                                @Pattern(regexp = "(?i)(age|created_at|gender_probability)$", message = "sort_by can only be age, created_at or gender_probability")
                                                String sortBy,

                                            @RequestParam(name = "order", required = false, defaultValue = "asc")
                                                @Pattern(regexp = "(?i)(asc|desc)$", message = "order can only be asc or desc")
                                                String order){
        Pageable pageable = PageRequest.of(0,Integer.MAX_VALUE);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=profiles.csv")
                .body(profileService.exportCsv(gender,ageGroup,countryId,minAge,maxAge,pageable));
    }


}