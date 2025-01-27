package com.nataliatsi.mavis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotBlank(message = "O username não pode ser vazio ou nulo")
        @Size(min = 4, max = 20, message = "O username deve ter entre 4 e 20 caracteres")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "A senha não pode ser vazia ou nula")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
        String password

) {
}
