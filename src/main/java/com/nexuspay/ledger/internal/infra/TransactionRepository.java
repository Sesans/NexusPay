package com.nexuspay.ledger.internal.infra;

import com.nexuspay.ledger.internal.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByCorrelationId(UUID correlationId);
}
