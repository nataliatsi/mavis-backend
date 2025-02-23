package com.nataliatsi.mavis.dto;

import java.time.LocalDate;
import java.util.List;

public record GetProfileDto(
        Long id,
        String fullName,
        LocalDate dateOfBirth,
        String phoneNumber,
        AddressDto address,
        LocationDto location,
        List<GetEmergencyContactDto> emergencyContacts,
        List<GetMedicalHistoryDto> medicalHistory
) {}

