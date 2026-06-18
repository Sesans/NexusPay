package com.nexuspay.composition.web;

import com.nexuspay.composition.application.UserService;
import com.nexuspay.composition.application.dto.AccountSummaryDTO;
import com.nexuspay.composition.application.dto.UserSummaryDTO;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;

    AccountSummaryDTO account = new AccountSummaryDTO(UUID.randomUUID(), 10000L, CurrencyCode.BRL);
    UserSummaryDTO dto = new UserSummaryDTO(UUID.randomUUID(), "Test", "VERIFIED", List.of(account));

    @Test
    @DisplayName("Should return the JSON with the user summary correctly")
    void getUserSummary_success() throws Exception {
        when(userService.getUserSummary(nullable(UUID.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dto.id().toString()));

        verify(userService, times(1)).getUserSummary(nullable(UUID.class));
    }
}