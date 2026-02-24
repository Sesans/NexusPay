package com.nexuspay.ledger.internal.infra;

import com.nexuspay.ledger.internal.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
