package com.nexuspay.ledger;

import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.api.dto.TransferResponseDTO;
import com.nexuspay.ledger.internal.application.TransactionService;
import com.nexuspay.ledger.internal.domain.model.TransactionCondition;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
public class LedgerFacade {
    private final TransactionService transactionService;

    public LedgerFacade(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            backoff = @Backoff(delay = 100)
    )
    public TransferResponseDTO transfer(TransferRequestDTO dto){
        Long transactionId = transactionService.processTransfer(dto);

        return new TransferResponseDTO(
                transactionId,
                dto.correlationId(),
                TransactionCondition.PROCESSED,
                LocalDateTime.now()
        );
    }
}
