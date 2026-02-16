package com.nataliatsi.mavis.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequestDTO(
        @NotBlank(message = "Current password cannot be empty or null")
        @NotNull
        String oldPassword,

        @NotBlank(message = "New password cannot be empty or null")
        @NotNull
        @Size(min = 8, max = 20, message = "Password must be between 4 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String newPassword
) {
}
