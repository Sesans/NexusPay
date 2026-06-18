package com.nexuspay.ledger.application;

import com.nexuspay.ledger.AccountFacade;
import com.nexuspay.ledger.application.dto.AccountDTO;
import com.nexuspay.ledger.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountFacadeImpl implements AccountFacade {
    private final AccountRepository accountRepository;

    public AccountFacadeImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDTO> getAccountSummariesByUserId(UUID userId) {
        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(account ->
                        new AccountDTO(
                                account.getId(),
                                account.getBalanceCents(),
                                account.getCurrencyCode()
                        )).toList();
    }
}