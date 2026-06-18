package com.nexuspay.composition.application;

import com.nexuspay.auth.UserFacade;
import com.nexuspay.auth.application.dto.UserDTO;
import com.nexuspay.auth.domain.model.UserStatus;
import com.nexuspay.ledger.AccountFacade;
import com.nexuspay.ledger.application.dto.AccountDTO;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserFacade userFacade;
    @Mock
    AccountFacade accountFacade;
    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Should retrieve the user and account summary successfully")
    void retrieveUserSummarySuccess(){
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(userId, "test", UserStatus.VERIFIED);
        AccountDTO accountDTO = new AccountDTO(userId, 1000L, CurrencyCode.BRL);
        when(userFacade.getUserById(userId)).thenReturn(userDTO);
        when(accountFacade.getAccountSummariesByUserId(userId)).thenReturn(List.of(accountDTO));

        var response = userService.getUserSummary(userId);

        assertEquals(userId, response.id());
        assertEquals(userDTO.name(), response.name());
        assertEquals(userDTO.status().toString(), response.status());
        assertNotNull(response.accounts());
        assertEquals(accountDTO.balance(), response.accounts().getFirst().balance());
        assertEquals(accountDTO.code(), response.accounts().getFirst().code());
    }
}