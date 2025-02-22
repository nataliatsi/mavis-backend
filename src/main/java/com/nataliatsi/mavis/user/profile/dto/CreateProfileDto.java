package com.nataliatsi.mavis.user.profile.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;


public record CreateProfileDto(
        @NotBlank(message = "Nome completo é obrigatório")
        @Size(max = 100, message = "Nome completo pode ter no máximo 100 caracteres")
        String fullName,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dateOfBirth,

        @NotBlank(message = "Número de telefone é obrigatório")
        @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Número de telefone inválido")
        String phoneNumber,

        @NotNull(message = "Endereço é obrigatório")
        AddressDto address,

        @NotNull(message = "A localização é obrigatória")
        LocationDto location,

        @NotEmpty(message = "Deve haver pelo menos um contato de emergência")
        List<@Valid EmergencyContactDto> emergencyContacts

) {
}
