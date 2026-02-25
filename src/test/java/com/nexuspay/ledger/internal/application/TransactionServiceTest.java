package com.nexuspay.ledger.internal.application;

import com.nexuspay.ledger.domain.valueobject.CurrencyCode;
import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.internal.domain.model.Account;
import com.nexuspay.ledger.internal.domain.model.LedgerEntry;
import com.nexuspay.ledger.internal.infra.AccountRepository;
import com.nexuspay.ledger.internal.infra.LedgerEntryRepository;
import com.nexuspay.ledger.internal.infra.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    LedgerEntryRepository entryRepository;
    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    Account acc1;
    Account acc2;
    TransferRequestDTO dto;

    @BeforeEach
    void setup(){
        acc1 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        acc2 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        ReflectionTestUtils.setField(acc1, "balanceCents", 1000L);
        ReflectionTestUtils.setField(acc1, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(acc2, "balanceCents", 500L);
        ReflectionTestUtils.setField(acc2, "id", UUID.randomUUID());

        dto = new TransferRequestDTO(acc1.getId(), acc2.getId(), UUID.randomUUID(), "", 100L);
    }

    @Test
    @DisplayName("Successfully process transaction")
    void processTransfer_success() {
        when(transactionRepository.existsByCorrelationId(dto.correlationId())).thenReturn(false);
        when(accountRepository.findById(dto.sourceId())).thenReturn(Optional.of(acc1));
        when(accountRepository.findById(dto.destinationId())).thenReturn(Optional.of(acc2));

        transactionService.processTransfer(dto);

        assertEquals(900L, acc1.getBalanceCents());
        verify(transactionRepository, times(1)).existsByCorrelationId(any());
        verify(entryRepository, times(2)).save(any(LedgerEntry.class));
    }
}