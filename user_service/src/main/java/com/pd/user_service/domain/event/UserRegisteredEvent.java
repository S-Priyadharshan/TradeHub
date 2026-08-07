package com.pd.user_service.domain.event;

import com.pd.user_service.domain.enums.AuthProvider;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String username,
        String email,
        LocalDateTime registeredAt,
        AuthProvider authProvider
) {
}
