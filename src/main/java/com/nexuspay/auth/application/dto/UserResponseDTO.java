package com.nexuspay.auth.application.dto;

import com.nexuspay.auth.domain.model.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String name,
        String email,
        UserStatus status,
        LocalDateTime createdAt,
        String token
) {
}
