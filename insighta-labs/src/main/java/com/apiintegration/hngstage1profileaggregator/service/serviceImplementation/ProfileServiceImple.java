package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.data.model.Country;
import com.apiintegration.hngstage1profileaggregator.data.model.Profile;
import com.apiintegration.hngstage1profileaggregator.data.model.Query;
import com.apiintegration.hngstage1profileaggregator.data.repository.ProfileRepository;
import com.apiintegration.hngstage1profileaggregator.dtos.request.GetProfilesRequest;
import com.apiintegration.hngstage1profileaggregator.dtos.response.*;
import com.apiintegration.hngstage1profileaggregator.exception.ProfileExistException;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.AgeApi;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.ProfileService;
import com.apiintegration.hngstage1profileaggregator.utils.Mapper;
import com.apiintegration.hngstage1profileaggregator.utils.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class ProfileServiceImple implements ProfileService {
    @Autowired
    private AgeApi ageApi;
    @Autowired
    private GeneralizeApi generalizeApi;
    @Autowired
    private NationalizeApi nationalizeApi;

    @Autowired
    private ProfileRepository profileRepository;



    @Override
    public ServiceResponse<ProfileResponse> createProfile(GetProfilesRequest getProfilesRequest) {

        Optional<Profile> profileOptional = profileRepository.findByName(getProfilesRequest.getName());
       if(profileOptional.isPresent()){
           ProfileResponse profileResponse= Mapper.mapProfileToProfileResponse(profileOptional.get());
           return Mapper.mapProfileResponseToServiceResponse(profileResponse,true);
       }
        CompletableFuture<AgeApiResponse> ageApiResponseCompletableFuture = CompletableFuture.supplyAsync(()->ageApi.getAge(getProfilesRequest.getName()));
        CompletableFuture<GenderizeApiResponse> genderizeApiResponseCompletableFuture = CompletableFuture.supplyAsync(()->generalizeApi.getGender(getProfilesRequest.getName()));
        CompletableFuture<NationalityApiResponse> nationalizeApiCompletableFuture = CompletableFuture.supplyAsync(()->nationalizeApi.getNationality(getProfilesRequest.getName()));
        CompletableFuture.allOf(ageApiResponseCompletableFuture, genderizeApiResponseCompletableFuture, nationalizeApiCompletableFuture).join();
       Profile profile= Mapper.mapApiResponsesToProfile(ageApiResponseCompletableFuture,genderizeApiResponseCompletableFuture,nationalizeApiCompletableFuture);
      Country country = determineCountry(nationalizeApiCompletableFuture.join().getCountry());
      Locale locale = new Locale("", country.getCountryId());
       profile.setCountryId(country.getCountryId());
       profile.setCountryProbability(country.getProbability());
       profile.setCountryName(locale.getDisplayCountry());
       profile.setAgeGroup(DetermineAgeGroup(ageApiResponseCompletableFuture.join().getAge()));
       profile.setAge(ageApiResponseCompletableFuture.join().getAge());
        profileRepository.save(profile);
        ProfileResponse response= Mapper.mapProfileToProfileResponse(profile);
        return Mapper.mapProfileResponseToServiceResponse(response,false);
    }

    @Override
    public ProfileResponse getProfileById(UUID id) {
         Optional<Profile> profileOptional = profileRepository.getProfilesById(id);
         if(profileOptional.isPresent()){
             return Mapper.mapProfileToProfileResponse(profileOptional.get());
         }
         throw new ProfileExistException("profile does not exist");
    }

    @Override
    public Page<Summary> getProfiles(String gender, String ageGroup, String countryId, Integer minAge, Integer maxAge, Float minGenderProbability, Float minCountyProbability, Pageable pageable) {
        Specification<Profile> spec = Specification.where((root, query, criteriaBuilder) -> null);
try {
    if (gender != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(cb.lower(root.get("gender")), gender.toLowerCase()));
    }
    if (countryId != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(cb.lower(root.get("countryId")), countryId.toLowerCase()));
    }
    if (ageGroup != null) {
        spec = spec.and((root, query, cb) ->
                cb.equal(cb.lower(root.get("ageGroup")), ageGroup.toLowerCase()));
    }
    if( minAge != null){
        spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("age"), (minAge)));
    }
    if( maxAge != null){
        spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("age"), (maxAge)));
    }
    if( minGenderProbability != null){
        spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("genderProbability"), (minGenderProbability)));
    }
    if( minCountyProbability != null){
        spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("countryProbability"),(minCountyProbability)));
    }
    Page<Profile> profiles = profileRepository.findAll(spec,pageable);
    return profiles.map(Mapper::mapProfileToSummary);

} catch (Exception e) {
    e.printStackTrace();
    throw e;
}


    }


    @Override
    public void deleteProfile(UUID id) {
            Optional<Profile> profileOptional = profileRepository.findById(id);
            if(profileOptional.isPresent()){
                profileRepository.delete(profileOptional.get());
            } else {
                throw new ProfileExistException("profile does not exist");
            }
    }

    @Override
    public Page<Summary> search(String note,Pageable pageable) {
        String[] splitterdNote =QueryParser.arrayOfNOtes(note);
        Query query = new Query();

        for(int i=0;i<splitterdNote.length;i++){
           String[] sortedArray = QueryParser.sortNote(splitterdNote,i);
           if(sortedArray==null)continue;
           QueryParser.getFieldName(sortedArray,query,splitterdNote,i);
        }
        Integer minAge=null;
        Integer maxAge=null;
        if (query.getYoung().getMin() != null) {
            minAge = query.getYoung().getMin();
        } else if (query.getAgeModifier().getMin() != null) {
            minAge = query.getAgeModifier().getMin();
        }

        if (query.getYoung().getMax() != null) {
            maxAge = query.getYoung().getMax();
        } else if (query.getAgeModifier().getMax() != null) {
            maxAge = query.getAgeModifier().getMax();
        }
        if (query.getGender() == null
                && query.getAgeGroup() == null
                && query.getCountryId() == null
                && minAge == null
                && maxAge == null) {
            throw new RuntimeException("Unable to interpret query");
        }
       return getProfiles(query.getGender(),query.getAgeGroup(),query.getCountryId(),minAge,maxAge,null,null,pageable);
    }

    @Override
    public byte[] exportCsv(String gender, String ageGroup, String countryId, Integer minAge, Integer maxAge,Pageable pageable) {
            Page<Summary> profiles = getProfiles(gender, ageGroup, countryId, minAge, maxAge, null, null, pageable);
            StringBuilder csv = new StringBuilder();
            csv.append("id,name,gender,age,age_group,country_id\n");
            for (Summary profile : profiles.getContent()) {
                csv.append(String.format("%s,%s,%s,%s,%s,%s\n",
                        profile.getId(),
                        profile.getName(),
                        profile.getGender(),
                        profile.getAge(),
                        profile.getAgeGroup(),
                        profile.getCountryId()
                ));
            }
            return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String DetermineAgeGroup(int age) {
        String ageGroup = "";
        if(age >=0 && age <= 12){
            ageGroup= "child";
        } else if (age >=13 && age <= 19 ){
            ageGroup= "teenager";
        } else if (age >=20 && age <= 59) {
            ageGroup= "adult";
        } else if (age>=60) {
            ageGroup= "senior";
        }
    return ageGroup;
    }

    private Country determineCountry(List<Country> countries) {
       float highestProbability = 0;
       Country countryWithHighestProbability=null;
        for (Country country : countries ){
            if (country.getProbability() > highestProbability){
                countryWithHighestProbability = country;
                highestProbability = country.getProbability();
            }
        }
        assert countryWithHighestProbability != null;
        return countryWithHighestProbability;
    }
}
