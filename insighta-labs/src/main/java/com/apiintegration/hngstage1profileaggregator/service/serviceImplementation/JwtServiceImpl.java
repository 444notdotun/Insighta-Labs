package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.data.model.Roles;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private  String secretKey;
    @Value("${jwt.expiration}")
    private  long expiration;
    @Override
    public String generateToken(Users username) {
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(username.getUserid())
                .issuedAt(new Date())
                .claim("role",username.getRole())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }
    public Roles getRoleFromToken(String token){
        String role = extractClaims(token).get("role", String.class);
        return Roles.valueOf(role);
    }

    @Override
    public boolean validateToken(String token) {
        try{
            extractClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey(){
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
