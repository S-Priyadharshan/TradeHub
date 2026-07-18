package com.pd.auth_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordRequest(
        @NotBlank(message = "Old Password cannot be blank")
        @Length(min=3,max=255,message = "Password must be between 3 and 255 characters")
        String currentPassword,


        @NotBlank(message = "New Password cannot be blank")
        @Length(min=3,max=255,message = "Password must be between 3 and 255 characters")
        String newPassword
) {
}
