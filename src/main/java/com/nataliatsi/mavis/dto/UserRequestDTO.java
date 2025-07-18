package com.nataliatsi.mavis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequestDTO(
        @NotBlank(message = "O username não pode ser vazio ou nulo")
        @Size(min = 4, max = 20, message = "O username deve ter entre 4 e 20 caracteres")
        String username,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "O número de telefone é obrigatório")
        String phoneNumber,

        @NotBlank(message = "A senha não pode ser vazia ou nula")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
        String password,

        @NotBlank(message = "O nome completo não pode ser vazio ou nulo")
        String fullName,

        @NotNull(message = "A data de nascimento é obrigatória")
        LocalDate birthDate,

        @Valid
        @NotNull(message = "Endereço é obrigatório")
        AddressRequestDTO address

) {
}
