package com.nexuspay.ledger.domain.exception;

import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(UUID accountId, Long attempt, Long current) {
        super(String.format("Account %s has insufficient funds. Attempted: %d, Available: %d", accountId, attempt, current));
    }
}
