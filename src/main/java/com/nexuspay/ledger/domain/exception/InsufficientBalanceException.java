package com.nexuspay.ledger.domain.exception;

import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(UUID accountId, Long attempt) {
        super(String.format("Account %s has insufficient funds. Attempted: %d", accountId, attempt));
    }
}
