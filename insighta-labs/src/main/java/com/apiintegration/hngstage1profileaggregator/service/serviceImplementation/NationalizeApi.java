package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.dtos.response.NationalityApiResponse;
import com.apiintegration.hngstage1profileaggregator.exception.ApiRequestLimitError;
import com.apiintegration.hngstage1profileaggregator.exception.ApiResponseException;
import com.apiintegration.hngstage1profileaggregator.exception.GenderNullException;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.NationalityApi;
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
public class NationalizeApi implements NationalityApi {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpClient client;

    @Override
    public NationalityApiResponse getNationality(String name) {
        String url = "https://api.nationalize.io?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        return fetchNationalityApiResponse(url);
    }

    private NationalityApiResponse fetchNationalityApiResponse(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
            return getNationalityApiResponse(response);


        } catch (IOException | InterruptedException e) {
            throw new ApiResponseException("api returned error");
        }
    }

    private NationalityApiResponse getNationalityApiResponse(HttpResponse<String> response) {
        if(response.statusCode() == 200) {
            NationalityApiResponse nationalityApiResponse= objectMapper.readValue(response.body(), NationalityApiResponse.class);
            if (nationalityApiResponse.getCountry()==null|| nationalityApiResponse.getCountry().isEmpty()){
                throw new GenderNullException("Nationalize returned an invalid response");
            }
            return nationalityApiResponse;
        }else {
            throw new ApiRequestLimitError("api returned request limit error");
        }
    }
}
