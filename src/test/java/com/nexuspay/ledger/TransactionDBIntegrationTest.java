package com.nexuspay.ledger;

import com.nexuspay.ledger.domain.valueobject.CurrencyCode;
import com.nexuspay.ledger.domain.valueobject.TransactionType;
import com.nexuspay.ledger.internal.domain.model.Account;
import com.nexuspay.ledger.internal.domain.model.LedgerEntry;
import com.nexuspay.ledger.internal.domain.model.Transaction;
import com.nexuspay.ledger.internal.domain.model.TransactionCondition;
import com.nexuspay.ledger.internal.infra.AccountRepository;
import com.nexuspay.ledger.internal.infra.LedgerEntryRepository;
import com.nexuspay.ledger.internal.infra.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class TransactionDBIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    LedgerEntryRepository entryRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void connectionEstablished(){
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should persist the ledger entry and update the balance")
    void PersistLedgerSuccess(){
        LocalDateTime time = LocalDateTime.now();
        Account account = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        accountRepository.save(account);

        Transaction transaction = new Transaction(UUID.randomUUID(), "", time, TransactionCondition.PROCESSED);
        transactionRepository.save(transaction);

        LedgerEntry ledgerEntry = new LedgerEntry(account.getId(), 1L, 1000L, TransactionType.CREDIT, "1010", time);
        entryRepository.save(ledgerEntry);

        Optional<LedgerEntry> entry = entryRepository.findTopByAccountIdOrderByIdDesc(ledgerEntry.getAccountId());
        assertThat(entry).isNotEmpty();
        assertThat(entry.get().getAmount()).isEqualTo(1000L);
        assertThat(entry.get().getEntryType()).isEqualTo(TransactionType.CREDIT);
    }
}
