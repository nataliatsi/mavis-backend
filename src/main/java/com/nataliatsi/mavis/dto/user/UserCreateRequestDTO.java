package com.nataliatsi.mavis.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @NotBlank(message = "Username cannot be empty or null")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Phone number must contain between 10 and 15 digits and may start with +"
        )
        String phoneNumber,

        @NotBlank(message = "Password cannot be empty or null")
        @Size(min = 8, max = 20, message = "Password must be between 4 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password
) {
}
