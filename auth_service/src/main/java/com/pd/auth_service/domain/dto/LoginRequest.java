package com.pd.auth_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotBlank(message = "Username cannot be blank")
        @Length(max = 100,message = "Length cannot exceed 1000")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Length(min=3,max=255,message = "Password must be between 3 and 255 characters")
        String password
) {
}
