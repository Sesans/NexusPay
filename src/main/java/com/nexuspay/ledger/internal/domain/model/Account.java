package com.nexuspay.ledger.internal.domain.model;

import com.nexuspay.ledger.internal.domain.valueobjetct.CurrencyCode;
import com.nexuspay.ledger.internal.domain.exception.InsufficientBalanceException;
import com.nexuspay.ledger.internal.domain.exception.InvalidTransactionAmountException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private CurrencyCode currencyCode;

    @Column(nullable = false)
    private Long balanceCents;

    @Version
    private int version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Account(){}

    public Account(UUID userId, CurrencyCode currencyCode){
        this.userId = userId;
        this.balanceCents = 0L;
        this.currencyCode = currencyCode;
        this.createdAt = LocalDateTime.now();
    }

    public void credit(Long amount) {
        if(amount == null || amount <=0)
            throw new InvalidTransactionAmountException("The amount must be a positive number!");
        this.balanceCents += amount;
    }

    public void debit(long amount) {
        if (amount <= 0)
            throw new InvalidTransactionAmountException("The amount must be a positive number!");
        if (amount > this.balanceCents)
            throw new InsufficientBalanceException(this.id, amount, this.balanceCents);

        this.balanceCents -= amount;
    }
}
