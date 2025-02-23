package com.nataliatsi.mavis.dto;

public record GetEmergencyContactDto(
        Long id,
        String name,
        String relationship,
        String phoneNumber,
        String email
) {
}