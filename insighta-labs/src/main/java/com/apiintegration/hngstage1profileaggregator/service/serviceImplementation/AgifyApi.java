package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.dtos.response.AgeApiResponse;
import com.apiintegration.hngstage1profileaggregator.exception.ApiRequestLimitError;
import com.apiintegration.hngstage1profileaggregator.exception.ApiResponseException;
import com.apiintegration.hngstage1profileaggregator.exception.GenderNullException;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.AgeApi;
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
public class AgifyApi implements AgeApi {
    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private HttpClient client;
    @Override
    public AgeApiResponse getAge(String name) {
        String url = "https://api.agify.io?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        return fetchAgeApiResponse(url);
    }

    private AgeApiResponse fetchAgeApiResponse(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return getAgeApiResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new ApiResponseException("api returns error");
        }
    }

    private AgeApiResponse getAgeApiResponse(HttpResponse<String> response) {
        if(response.statusCode() == 200) {
            AgeApiResponse apiResponse= objectMapper.readValue(response.body(), AgeApiResponse.class);
            if (apiResponse.getAge()==null){
                throw new GenderNullException(" Agify returned an invalid response ");
            }
            return apiResponse;
        }else {
            throw new ApiRequestLimitError("api returns limit error");
        }
    }
}
