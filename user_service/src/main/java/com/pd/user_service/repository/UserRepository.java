package com.pd.user_service.repository;

import com.pd.user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,UUID> {

    Optional<User> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
