package com.nexuspay.ledger.web;

import com.nexuspay.ledger.LedgerFacade;
import com.nexuspay.ledger.application.dto.TransferRequestDTO;
import com.nexuspay.ledger.application.dto.TransferResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ledger")
public class TransactionController {
    private final LedgerFacade ledgerFacade;

    public TransactionController(LedgerFacade ledgerFacade) {
        this.ledgerFacade = ledgerFacade;
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('VERIFIED')")
    public TransferResponseDTO transfer(@RequestBody @Valid TransferRequestDTO dto){
        return ledgerFacade.transfer(dto);
    }
}
