package com.nexuspay.ledger.application;

import com.nexuspay.ledger.application.dto.AccountResponseDTO;
import com.nexuspay.ledger.domain.exception.BusinessException;
import com.nexuspay.ledger.domain.model.Account;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import com.nexuspay.ledger.domain.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountCreationUseCase {
    private final AccountRepository accountRepository;

    public AccountCreationUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponseDTO createAccount(UUID userId, CurrencyCode code){
        if(accountRepository.existsByUserIdAndCurrencyCode(userId, code))
            throw new BusinessException("User already have an account with this Currency Code");

        Account account = new Account(userId, code);
        accountRepository.save(account);
        LocalDateTime timestamp = LocalDateTime.now();
        return new AccountResponseDTO(
                userId,
                code,
                timestamp
        );
    }
}
