package com.nataliatsi.mavis.user.profile.dto;


import com.nataliatsi.mavis.user.profile.entities.EmergencyContact;
import com.nataliatsi.mavis.user.profile.entities.MedicalHistory;

import java.time.LocalDate;
import java.util.List;

public record GetProfileDto(
        Long id,
        String fullName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        AddressDto address,
        List<EmergencyContact> emergencyContacts,
        List<MedicalHistory> medicalHistory
) {}

