package com.pd.auth_service.repository;

import com.pd.auth_service.domain.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByKeycloakId(UUID keycloakId);
    Optional<AuthUser> findByEmail(String email);
}
