package com.nexuspay.ledger.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record TransferRequestDTO(
        @NotNull(message = "Source account cannot be null")
        UUID sourceId,
        @NotNull(message = "Destination account cannot be null")
        UUID destinationId,
        @NotNull(message = "Correlation ID cannot be null")
        UUID correlationId,
        String description,
        @Positive(message = "Amount must have a positive number")
        Long amount
) {
}
