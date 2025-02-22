package com.nataliatsi.mavis.user.profile.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateMedicalHistoryDto(
        @NotEmpty(message = "A lista de medicamentos não pode estar vazia")
        List<String> medications,

        @NotEmpty(message = "A lista de alergias não pode estar vazia")
        List<String> allergies,

        @NotEmpty(message = "A lista de condições pré-existentes não pode estar vazia")
        List<String> preExistingConditions

) {
}
