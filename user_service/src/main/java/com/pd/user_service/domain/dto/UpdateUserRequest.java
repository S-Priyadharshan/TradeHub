package com.pd.user_service.domain.dto;

import java.time.LocalDate;

public record UpdateUserRequest(
        String fullName,
        String phoneNumber,
        LocalDate dateOfBirth
) {}