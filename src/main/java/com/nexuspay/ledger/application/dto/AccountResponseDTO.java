package com.nexuspay.ledger.application.dto;

import com.nexuspay.ledger.domain.model.CurrencyCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID userId,
        CurrencyCode code,
        LocalDateTime timestamp
) {
}