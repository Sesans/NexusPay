package com.nexuspay.ledger;

import com.nexuspay.ledger.api.exception.DuplicateTransactionException;
import com.nexuspay.ledger.domain.valueobject.CurrencyCode;
import com.nexuspay.ledger.domain.valueobject.TransactionType;
import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.internal.application.TransactionService;
import com.nexuspay.ledger.internal.domain.model.Account;
import com.nexuspay.ledger.internal.domain.model.LedgerEntry;
import com.nexuspay.ledger.internal.domain.model.Transaction;
import com.nexuspay.ledger.internal.domain.model.TransactionCondition;
import com.nexuspay.ledger.internal.infra.AccountRepository;
import com.nexuspay.ledger.internal.infra.LedgerEntryRepository;
import com.nexuspay.ledger.internal.infra.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class TransactionDBIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    LedgerEntryRepository entryRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    LedgerFacade ledgerFacade;

    @MockitoSpyBean
    TransactionService transactionService;



    Account acc1 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
    Account acc2 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
    TransferRequestDTO dto;

    @Test
    void connectionEstablished(){
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setup(){
        acc1.credit(1000L);
        acc2.credit(500L);
        accountRepository.save(acc1);
        accountRepository.save(acc2);
        dto = new TransferRequestDTO(acc1.getId(), acc2.getId(), UUID.randomUUID(), "", 100L);
    }

    @Test
    @DisplayName("Should persist the ledger entry successfully")
    void persistLedgerSuccess(){
        LocalDateTime time = LocalDateTime.now();

        Transaction transaction = new Transaction(UUID.randomUUID(), "", time, TransactionCondition.PROCESSED);
        transactionRepository.save(transaction);

        LedgerEntry ledgerEntry = new LedgerEntry(
                acc1.getId(),
                transaction.getId(),
                1000L,
                TransactionType.CREDIT,
                "1010",
                time);
        entryRepository.save(ledgerEntry);

        assertThat(entryRepository.findTopByAccountIdOrderByIdDesc(ledgerEntry.getAccountId()))
                .hasValueSatisfying(entry -> {
                    assertThat(entry.getAmount()).isEqualTo(1000L);
                    assertThat(entry.getEntryType()).isEqualTo(TransactionType.CREDIT);
                });
    }

    @Test
    @DisplayName("Should use the facade to process a transaction successfully")
    void facadeProcessTransfer(){
        ledgerFacade.transfer(dto);

        // Assert the updated balance
        Account updatedAcc1 = accountRepository.findById(acc1.getId()).orElseThrow();
        Account updatedAcc2 = accountRepository.findById(acc2.getId()).orElseThrow();

        assertThat(updatedAcc1.getBalanceCents()).isEqualTo(900L);
        assertThat(updatedAcc2.getBalanceCents()).isEqualTo(600L);

        // Assert that the transaction is registered
        assertThat(transactionRepository.existsByCorrelationId(dto.correlationId())).isTrue();

        // Assert there are only 2 entries
        Long txId = transactionRepository.findByCorrelationId(dto.correlationId()).get().getId();
        assertThat(entryRepository.findAllByTransactionId(txId).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should process a transaction, but fail on the first try and then calling the Retry")
    void facadeProcessTransferRetry(){
        doThrow(new OptimisticLockingFailureException("Simulated error")).doCallRealMethod()
                        .when(transactionService).processTransfer(any());

        ledgerFacade.transfer(dto);

        Account updatedAcc1 = accountRepository.findById(acc1.getId()).get();
        assertThat(updatedAcc1.getBalanceCents()).isEqualTo(900L);
        verify(transactionService, times(2)).processTransfer(any());
    }

    @Test
    @DisplayName("Should receive a transaction and throw DuplicateTransactionException")
    void facadeProcessTransferThrow(){
        ledgerFacade.transfer(dto);

        Account updatedAcc1 = accountRepository.findById(acc1.getId()).get();
        assertThat(updatedAcc1.getBalanceCents()).isEqualTo(900L);

        DuplicateTransactionException ex = assertThrows(DuplicateTransactionException.class,
                () -> ledgerFacade.transfer(dto));

        assertEquals("This transaction has already been processed: " + dto.correlationId(), ex.getMessage());
    }
}