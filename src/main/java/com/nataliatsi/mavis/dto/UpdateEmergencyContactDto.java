package com.nataliatsi.mavis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateEmergencyContactDto(
        @Size(max = 100, message = "Nome pode ter no máximo 100 caracteres")
        String name,

        @Size(max = 50, message = "Relacionamento pode ter no máximo 50 caracteres")
        String relationship,

        @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Número de telefone inválido")
        String phoneNumber,

        @Email(message = "Email deve ser válido")
        String email
) {
}
