package com.pd.api_gateway.domain.dto;

public record JwtPrincipal(
        String userId,
        String username,
        String role,
        String provider,
        String accountStatus
) {
}
