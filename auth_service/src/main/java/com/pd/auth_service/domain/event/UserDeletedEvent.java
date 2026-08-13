package com.pd.auth_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDeletedEvent(
        UUID userId,
        LocalDateTime deletedAt
) {
}
