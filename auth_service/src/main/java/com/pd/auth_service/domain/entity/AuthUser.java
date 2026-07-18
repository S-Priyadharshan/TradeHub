package com.pd.auth_service.domain.entity;

import com.pd.auth_service.domain.enums.AccountStatus;
import com.pd.auth_service.domain.enums.AuthProvider;
import com.pd.auth_service.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="auth_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthUser {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",updatable = false,nullable = false)
    private UUID userId;

    @Column(name="keycloak_id",updatable=false)
    private UUID keycloakId;

    @Column(name="username",nullable = false,unique = true)
    private String username;

    @Column(name="email",nullable = false,unique = true)
    private String email;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name = "auth_provider",nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "account_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @EqualsAndHashCode.Include
    @CreatedDate
    @Column(name = "created_at",updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}
