package com.nexuspay.ledger.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LedgerEntryTest {
    LedgerEntry ledgerEntry;
    static final Long VALID_AMOUNT = 100L;
    static final Long TRANSACTION_ID = 1L;
    static final String PREVIOUS_HASH = "test";
    static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @Test
    @DisplayName("Verify if the calculateHash create and sets the currentHash properly")
    void ledgerCreationSuccess(){
        ledgerEntry = new LedgerEntry(UUID.randomUUID(), TRANSACTION_ID, VALID_AMOUNT, TransactionType.CREDIT,  PREVIOUS_HASH, TIMESTAMP);
        assertNotNull(ledgerEntry.getCurrentHash());
    }
}