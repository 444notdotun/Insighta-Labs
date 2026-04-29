package com.apiintegration.hngstage1profileaggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
@Configuration
public class Config {

    @Bean
    public HttpClient client() {
        return HttpClient.newHttpClient();
    }

}
