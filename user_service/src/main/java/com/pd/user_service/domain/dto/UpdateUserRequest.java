package com.pd.user_service.domain.dto;

public record UpdateUserRequest(
        String fullName,
        String phoneNumber
) {}