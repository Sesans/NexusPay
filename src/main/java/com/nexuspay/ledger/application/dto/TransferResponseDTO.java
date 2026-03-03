package com.nexuspay.ledger.application.dto;

import com.nexuspay.ledger.domain.model.TransactionCondition;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponseDTO(
        Long transactionId,
        UUID correlationId,
        TransactionCondition status,
        LocalDateTime timestamp
) {
}
