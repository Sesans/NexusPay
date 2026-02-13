package com.nexuspay.ledger.internal.domain.model;

import com.nexuspay.ledger.internal.domain.valueobjetct.CurrencyCode;
import com.nexuspay.ledger.internal.domain.exception.InsufficientBalanceException;
import com.nexuspay.ledger.internal.domain.exception.InvalidTransactionAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account;
    static final Long VALID_AMOUNT = 100L;
    static final Long INVALID_AMOUNT = -100L;

    @BeforeEach
    void setup(){
        account = new Account(UUID.randomUUID(), CurrencyCode.BRL);
    }
    @Test
    @DisplayName("Should add the amount to the balance successfully")
    void creditMethodSuccess(){
        account.credit(VALID_AMOUNT);

        assertEquals(VALID_AMOUNT, account.getBalanceCents());
    }

    @Test
    @DisplayName("Should throw InvalidTransactionAmountException when amount is a negative number")
    void creditInvalidAmount(){
        assertThrows(InvalidTransactionAmountException.class,
                ()-> account.credit(INVALID_AMOUNT));
    }

    @Test
    @DisplayName("Should subtract the amount from the balance successfully")
    void debitMethodSuccess(){
        account.credit(VALID_AMOUNT);
        account.debit(VALID_AMOUNT);

        assertEquals(0L, account.getBalanceCents());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when balance is lower than amount")
    void debitInsufficientBalance(){
        long higherValidAmount = VALID_AMOUNT + 10L;
        account.credit(VALID_AMOUNT);
        assertThrows(InsufficientBalanceException.class,
                ()-> account.debit(higherValidAmount));
    }

    @Test
    @DisplayName("Should throw InvalidTransactionAmountException when amount <= 0")
    void debitInvalidAmount(){
        assertThrows(InvalidTransactionAmountException.class,
                ()-> account.debit(INVALID_AMOUNT));
    }
}