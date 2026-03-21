package com.nexuspay.ledger.application.dto;

import com.nexuspay.ledger.domain.model.CurrencyCode;

public record AccountRequestDTO(
        CurrencyCode code
) {
}