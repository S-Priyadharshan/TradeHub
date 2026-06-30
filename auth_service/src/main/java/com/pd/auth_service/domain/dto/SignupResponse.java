package com.pd.auth_service.domain.dto;

import java.util.UUID;

public record SignupResponse(
        UUID userId,
        String username
) {
}
