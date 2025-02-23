package com.nataliatsi.mavis.dto;


import java.time.LocalDateTime;
import java.util.List;

public record GetMedicalHistoryDto(
        Integer version,
        List<String> medications,
        List<String> allergies,
        List<String> preExistingConditions,
        LocalDateTime createdAt
) {
}
