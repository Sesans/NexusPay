package com.nexuspay.ledger;

import com.nexuspay.ledger.application.dto.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface AccountFacade {
    List<AccountDTO> getAccountSummariesByUserId(UUID userId);
}