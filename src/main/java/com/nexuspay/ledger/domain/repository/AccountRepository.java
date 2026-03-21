package com.nexuspay.ledger.domain.repository;

import com.nexuspay.ledger.domain.model.Account;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByUserIdAndCurrencyCode(UUID userId, CurrencyCode code);
}
