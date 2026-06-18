package com.nexuspay.composition.application.dto;

import com.nexuspay.ledger.domain.model.CurrencyCode;

import java.util.UUID;

public record AccountSummaryDTO(
        UUID id,
        Long balance,
        CurrencyCode code
) {
}
