package com.nexuspay.ledger.api.dto;

import java.util.UUID;

public record TransferRequestDTO(
        UUID sourceId,
        UUID destinationId,
        UUID correlationId,
        String description,
        Long amount
) {
}
