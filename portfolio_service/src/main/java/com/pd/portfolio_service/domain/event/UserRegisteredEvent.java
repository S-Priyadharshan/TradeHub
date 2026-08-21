package com.pd.portfolio_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String username,
        String email,
        LocalDateTime registeredAt
) {
}

