package com.nexuspay.ledger.application.dto;

import com.nexuspay.ledger.domain.model.CurrencyCode;

import java.util.UUID;

public record AccountDTO(
        UUID id,
        Long balance,
        CurrencyCode code
) {
}