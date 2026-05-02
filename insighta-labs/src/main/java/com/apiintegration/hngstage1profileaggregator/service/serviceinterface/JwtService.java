package com.apiintegration.hngstage1profileaggregator.service.serviceinterface;

import com.apiintegration.hngstage1profileaggregator.data.model.Roles;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;

public interface JwtService {
    String generateToken(Users users);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    Roles getRoleFromToken(String token);
    String generateStateToken(String redirectUrl);
    String getRedirectUrlFromStateToken(String token);


}
