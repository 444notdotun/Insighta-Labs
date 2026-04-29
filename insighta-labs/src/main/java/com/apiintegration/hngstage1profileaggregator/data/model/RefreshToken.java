package com.apiintegration.hngstage1profileaggregator.data.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true,nullable = false)
    private String token;
    @CreationTimestamp
    private String userId;
    private LocalDateTime expiresAt;
    private boolean isRevoked;


}
