package com.nataliatsi.mavis.user.profile.dto;

public record GetEmergencyContactDto(
        Long id,
        String name,
        String relationship,
        String phoneNumber,
        String email
) {
}