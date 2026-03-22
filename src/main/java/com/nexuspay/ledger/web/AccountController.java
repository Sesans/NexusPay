package com.nexuspay.ledger.web;

import com.nexuspay.ledger.application.AccountCreationUseCase;
import com.nexuspay.ledger.application.dto.AccountRequestDTO;
import com.nexuspay.ledger.application.dto.AccountResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountCreationUseCase creationUseCase;

    public AccountController(AccountCreationUseCase useCase) {
        this.creationUseCase = useCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('VERIFIED')")
    public AccountResponseDTO create(@AuthenticationPrincipal UUID userId, @RequestBody @Valid AccountRequestDTO dto){
        return creationUseCase.createAccount(userId , dto.code());
    }
}
