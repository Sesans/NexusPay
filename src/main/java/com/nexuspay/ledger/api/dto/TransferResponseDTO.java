package com.nexuspay.ledger.api.dto;

import com.nexuspay.ledger.internal.domain.model.TransactionCondition;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponseDTO(
        Long transactionId,
        UUID correlationId,
        TransactionCondition status,
        LocalDateTime timestamp
) {
}
