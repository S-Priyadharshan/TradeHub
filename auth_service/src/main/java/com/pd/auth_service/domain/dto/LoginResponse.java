package com.pd.auth_service.domain.dto;

public record LoginResponse(
        String token,
        long expiresIn,
        String refreshToken
) {
}
