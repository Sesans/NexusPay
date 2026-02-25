package com.nexuspay.ledger.api.exception;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String correlationId) {
        super("This transaction has already been processed: " + correlationId);
    }
}