package com.nexuspay.ledger.internal.application;

import com.nexuspay.ledger.internal.domain.valueobjetct.TransferRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Transactional
    public void processTransfer(TransferRequestDTO dto){

    }
}
