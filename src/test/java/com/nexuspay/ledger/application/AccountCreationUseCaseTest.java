package com.nexuspay.ledger.application;

import com.nexuspay.ledger.application.dto.AccountResponseDTO;
import com.nexuspay.ledger.domain.exception.BusinessException;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import com.nexuspay.ledger.domain.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCreationUseCaseTest {
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountCreationUseCase creationUseCase;

    @Test
    @DisplayName("Must successfully create an account with a currency code not yet used by the user")
    void createAccountSuccess(){
        when(accountRepository.existsByUserIdAndCurrencyCode(any(UUID.class), any(CurrencyCode.class)))
                .thenReturn(false);
        UUID userId = UUID.randomUUID();
        CurrencyCode cc = CurrencyCode.EUR;

        AccountResponseDTO dto = creationUseCase.createAccount(userId, cc);

        assertEquals(userId, dto.userId());
        assertEquals(cc, dto.code());
    }

    @Test
    @DisplayName("Must throw an Business exception when trying an already used pair of ID + currency code")
    void createAccountException(){
        when(accountRepository.existsByUserIdAndCurrencyCode(any(UUID.class), any(CurrencyCode.class)))
                .thenReturn(true);
        UUID userId = UUID.randomUUID();
        CurrencyCode cc = CurrencyCode.EUR;

        BusinessException ex = assertThrows(BusinessException.class,
                () -> creationUseCase.createAccount(userId, cc));

        assertEquals("User already have an account with this Currency Code", ex.getMessage());
    }
}