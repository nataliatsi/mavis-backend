package com.nataliatsi.mavis.dto.user;

import com.nataliatsi.mavis.dto.RoleDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record UserCreateResponseDTO(
        String userId,
        String username,
        String email,
        String phoneNumber,
        Set<RoleDTO> roles,
        LocalDateTime createdAt
) {
}
