package com.nataliatsi.mavis.dto.user;

import com.nataliatsi.mavis.dto.RoleDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserCreateResponseDTO(
        UUID userId,
        String username,
        String email,
        String phoneNumber,
        Set<RoleDTO> roles,
        LocalDateTime createdAt
) {
}
