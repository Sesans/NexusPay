package com.nexuspay.composition.application.dto;

import java.util.List;
import java.util.UUID;

public record UserSummaryDTO(
        UUID id,
        String name,
        String status,
        List<AccountSummaryDTO> accounts
) {
}
