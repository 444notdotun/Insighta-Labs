package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.dtos.response.GenderizeApiResponse;
import com.apiintegration.hngstage1profileaggregator.exception.ApiRequestLimitError;
import com.apiintegration.hngstage1profileaggregator.exception.ApiResponseException;
import com.apiintegration.hngstage1profileaggregator.exception.GenderNullException;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.GenderApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GeneralizeApi implements GenderApi {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HttpClient client;
    @Override
    public GenderizeApiResponse getGender(String name) {
        String url = "https://api.genderize.io?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        return fetchGenderizeApiResponse(url);

    }

    private GenderizeApiResponse fetchGenderizeApiResponse(String url) {
        try  {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return getGenderizeApiResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new ApiResponseException("api return error");
        }
    }

    private GenderizeApiResponse getGenderizeApiResponse(HttpResponse<String> response) {
        if(response.statusCode() == 200) {
            GenderizeApiResponse genderizeApiResponse = objectMapper.readValue(response.body(), GenderizeApiResponse.class);
            if (genderizeApiResponse.getGender()==null||genderizeApiResponse.getCount()==0){
                throw new GenderNullException("Genderize returned an invalid response ");
            }
            return genderizeApiResponse;
        }else {
            throw new ApiRequestLimitError("api returned limit error");
        }
    }
}
