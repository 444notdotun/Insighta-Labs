package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.model.Roles;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.dtos.request.GithubRequest;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(Users users);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    Roles getRoleFromToken(String token);
    String generateStateToken(String redirectUrl, boolean isWeb);
    Claims ValidateStateToken(String token);


    String generateClientToken(String key, Boolean isWeb, String redirectUrl);
}
