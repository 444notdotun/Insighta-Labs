package com.apiintegration.hngstage1profileaggregator.data.repository;

import com.apiintegration.hngstage1profileaggregator.data.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
}
