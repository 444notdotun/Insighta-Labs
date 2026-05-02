package com.apiintegration.hngstage1profileaggregator.service.serviceImplementation;

import com.apiintegration.hngstage1profileaggregator.data.model.RefreshToken;
import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import com.apiintegration.hngstage1profileaggregator.data.repository.RefreshTokenRepository;
import com.apiintegration.hngstage1profileaggregator.data.repository.UsersRepository;
import com.apiintegration.hngstage1profileaggregator.dtos.response.AuthResponse;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.JwtService;
import com.apiintegration.hngstage1profileaggregator.service.serviceinterface.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
    public class RefreshTokenImpl implements RefreshTokenService {
        @Autowired
        private JwtService jwtService;
        @Autowired
        private UsersRepository usersRepository;
        @Autowired
        private RefreshTokenRepository refreshTokenRepository;

        @Override
        public String generateRefreshToken(Users user) {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUserId(user.getUserid());
            refreshToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
            refreshToken.setRevoked(false);
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        }

        @Override
        public RefreshToken validateRefreshToken(String token) {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));
            if (refreshToken.isRevoked()) {
                throw new RuntimeException("Refresh token has been revoked");
            }
            if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Refresh token has expired");
            }
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }

    @Override
    public AuthResponse rotateRefreshToken(String token) {
        RefreshToken refreshToken = validateRefreshToken(token);
        Users user = usersRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String newJwt = jwtService.generateToken(user);
        String newRefreshToken = generateRefreshToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newJwt);
        authResponse.setRefreshToken(newRefreshToken);
        return authResponse;
    }

}
