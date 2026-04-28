package com.apiintegration.hngstage1profileaggregator.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true,nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String token;
    private String userId;
    private Date expiresAt;
    private boolean isRevoked;

}
