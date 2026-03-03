package com.nexuspay.ledger.domain.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AccountNotFoundException extends RuntimeException {
    private final UUID accountId;

    public AccountNotFoundException(UUID accountId) {
        super(String.format("Account with ID %s was not found in our ledger.", accountId));
        this.accountId = accountId;
    }
}
