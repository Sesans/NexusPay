package com.nexuspay.ledger.internal.application;

import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.api.exception.AccountNotFoundException;
import com.nexuspay.ledger.api.exception.DuplicateTransactionException;
import com.nexuspay.ledger.domain.valueobject.TransactionType;
import com.nexuspay.ledger.internal.domain.model.Account;
import com.nexuspay.ledger.internal.domain.model.LedgerEntry;
import com.nexuspay.ledger.internal.domain.model.Transaction;
import com.nexuspay.ledger.internal.domain.model.TransactionCondition;
import com.nexuspay.ledger.internal.infra.AccountRepository;
import com.nexuspay.ledger.internal.infra.LedgerEntryRepository;
import com.nexuspay.ledger.internal.infra.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final LedgerEntryRepository entryRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, LedgerEntryRepository entryRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Long processTransfer(TransferRequestDTO dto) {
        if(transactionRepository.existsByCorrelationId(dto.correlationId()))
            throw new DuplicateTransactionException(dto.correlationId().toString());

        //UUID ordering to guarantee the database reading pattern avoiding deadlocks
        UUID firstId = dto.sourceId().compareTo(dto.destinationId()) < 0 ?dto.sourceId() : dto.destinationId();
        UUID secondId = firstId.equals(dto.sourceId()) ? dto.destinationId() : dto.sourceId();

        Account firstAccount = accountRepository.findById(firstId)
                .orElseThrow(() -> new AccountNotFoundException(firstId));
        Account secondAccount = accountRepository.findById(secondId)
                .orElseThrow(() -> new AccountNotFoundException(secondId));

        Account source = firstAccount.getId().equals(dto.sourceId()) ? firstAccount : secondAccount;
        Account destination = firstAccount.getId().equals(dto.destinationId()) ? firstAccount : secondAccount;

        LocalDateTime timestamp = LocalDateTime.now();

        source.debit(dto.amount());
        destination.credit(dto.amount());

        Transaction transaction = new Transaction(dto.correlationId(), dto.description(), timestamp, TransactionCondition.PROCESSED);
        transactionRepository.save(transaction);

        createAndSaveEntry(source, transaction.getId(), dto.amount(), TransactionType.DEBIT, timestamp);
        createAndSaveEntry(destination, transaction.getId(), dto.amount(), TransactionType.CREDIT, timestamp);

        return transaction.getId();
    }

    private String getPreviousHash(Account account){
        return entryRepository.findTopByAccountIdOrderByIdDesc(account.getId())
                .map(LedgerEntry::getCurrentHash)
                .orElse(account.getId().toString());
    }

    private void createAndSaveEntry(Account acc, Long transactionId, Long amount, TransactionType type, LocalDateTime timestamp){
        String prevHash = getPreviousHash(acc);
        LedgerEntry ledgerEntry = new LedgerEntry(acc.getId(), transactionId, amount, type, prevHash, timestamp);
        entryRepository.save(ledgerEntry);
    }
}
