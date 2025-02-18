package com.nataliatsi.mavis.user.profile.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileDto(
        @Size(max = 100, message = "Nome completo pode ter no máximo 100 caracteres")
        String fullName,

        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dateOfBirth,

        @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Número de telefone inválido")
        String phoneNumber,

        AddressDto address
) {
}
