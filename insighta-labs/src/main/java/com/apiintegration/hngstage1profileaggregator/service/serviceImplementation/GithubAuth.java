package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.JwtService;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.OAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubAuth implements OAuth {
    @Value("${CLIENT_ID}")
    private String clientId;
    @Autowired
    private JwtService jwtService;
    @Override
    public String getAuthUrl() {
        return "https://github.com/login/oauth/access_token";
    }


    @Override
    public String getRedirectUrl(String codeChallenge, String redirectUrl) {

        if(redirectUrl!=null && !redirectUrl.isEmpty()){
         String  state = jwtService.generateStateToken(redirectUrl);
            return String.format(
                    "https://github.com/login/oauth/authorize?client_id=%s&code_challenge=%s&code_challenge_method=S256&state=%s",
                    clientId, codeChallenge, state
            );

        }

        return String.format(
                "https://github.com/login/oauth/authorize?client_id=%s&code_challenge=%s&code_challenge_method=S256",
                clientId, codeChallenge
        );
    }


}
