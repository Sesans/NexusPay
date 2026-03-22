package com.nexuspay.ledger.web;

import com.nexuspay.ledger.application.AccountCreationUseCase;
import com.nexuspay.ledger.application.dto.AccountResponseDTO;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import com.nexuspay.shared.security.TokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    AccountCreationUseCase useCase;

    @MockitoBean
    TokenValidator tokenValidator;

    UUID userId = UUID.randomUUID();
    AccountResponseDTO responseDTO = new AccountResponseDTO(userId, CurrencyCode.JPY, LocalDateTime.now());

    @Test
    void createMethodSuccess() throws Exception {
        when(useCase.createAccount(nullable(UUID.class), any(CurrencyCode.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "code": "JPY"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.code").value("JPY"));

        verify(useCase, times(1)).createAccount(any(), any());
    }
}