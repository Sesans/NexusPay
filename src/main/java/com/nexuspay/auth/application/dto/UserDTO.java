package com.nexuspay.auth.application.dto;

import com.nexuspay.auth.domain.model.UserStatus;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        UserStatus status
) {
}
