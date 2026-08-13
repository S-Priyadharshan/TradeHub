package com.pd.auth_service.repository;

import com.pd.auth_service.domain.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

    Optional<AuthUser> findByUserId(UUID userId);
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByKeycloakId(UUID keycloakId);
    Optional<AuthUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
