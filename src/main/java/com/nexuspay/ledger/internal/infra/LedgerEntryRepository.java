package com.nexuspay.ledger.internal.infra;

import com.nexuspay.ledger.internal.domain.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    Optional<LedgerEntry> findTopByAccountIdOrderByIdDesc(UUID id);
}
