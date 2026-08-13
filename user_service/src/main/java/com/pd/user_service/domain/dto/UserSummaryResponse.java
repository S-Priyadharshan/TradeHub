package com.pd.user_service.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSummaryResponse(
        UUID userId,
        String username,
        String email,
        LocalDateTime createdAt
) {}