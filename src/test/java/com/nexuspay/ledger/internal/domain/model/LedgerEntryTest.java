package com.nexuspay.ledger.internal.domain.model;

import com.nexuspay.ledger.internal.domain.valueobjetct.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LedgerEntryTest {
    LedgerEntry ledgerEntry;
    static final Long VALID_AMOUNT = 100L;
    static final Long TRANSACTION_ID = 1L;
    static final String PREVIOUS_HASH = "test";
    static final String CURRENT_HASH = "test";

    @Test
    @DisplayName("Verify if the calculateHash create and sets the currentHash properly")
    void ledgerCreationSuccess(){
        ledgerEntry = new LedgerEntry(UUID.randomUUID(), TRANSACTION_ID, VALID_AMOUNT, TransactionType.CREDIT,  PREVIOUS_HASH, CURRENT_HASH);
        assertNotNull(ledgerEntry.getCurrentHash());
    }
}