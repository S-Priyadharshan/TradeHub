package com.pd.auth_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "refreshTokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="token_id",updatable = false,nullable = false)
    private UUID tokenId;

    @Column(name = "user_id",updatable = false,nullable = false)
    private UUID userId;

    @Column(name = "token_hash",updatable = false,nullable = false)
    private String tokenHash;

    @Column(name="expires_At",updatable = false,nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked",nullable = false)
    private Boolean revoked;

    @Column(name = "created_at",updatable = false,nullable = false)
    private LocalDateTime createdAt;
}
