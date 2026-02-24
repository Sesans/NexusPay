package com.nexuspay.ledger;

import com.nexuspay.ledger.dto.TransferRequestDTO;
import com.nexuspay.ledger.internal.application.TransactionService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class LedgerFacade {
    private final TransactionService transactionService;

    public LedgerFacade(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void transfer(TransferRequestDTO dto){
        transactionService.processTransfer(dto);
    }
}
