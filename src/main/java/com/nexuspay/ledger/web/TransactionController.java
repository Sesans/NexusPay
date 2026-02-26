package com.nexuspay.ledger.web;

import com.nexuspay.ledger.LedgerFacade;
import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.api.dto.TransferResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public TransferResponseDTO transfer(@RequestBody @Valid TransferRequestDTO dto){
        return ledgerFacade.transfer(dto);
    }
}
