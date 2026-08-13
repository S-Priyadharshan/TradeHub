package com.pd.user_service.domain.dto;

import com.pd.user_service.domain.enums.AuthProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(

        @NotNull(message = "User ID cannot be null")
        UUID userId,

        @NotBlank(message = "Username cannot be blank")
        @Length(max = 100, message = "Username cannot exceed 100 characters")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be a valid email address")
        @Length(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @Length(max = 255, message = "Full name cannot exceed 255 characters")
        String fullName,

        @Length(max = 20, message = "Phone number cannot exceed 20 characters")
        String phoneNumber,

        LocalDate dateOfBirth,

        @NotNull(message = "Authentication provider cannot be null")
        AuthProvider authProvider,

        @NotNull(message = "Created timestamp cannot be null")
        LocalDateTime createdAt
) {}