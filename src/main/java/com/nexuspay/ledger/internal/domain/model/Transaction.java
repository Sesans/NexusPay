package com.nexuspay.ledger.internal.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID correlationId;

    @Column(nullable = false, updatable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionCondition status;

    protected Transaction(){}

    public Transaction(UUID correlationId, String description, LocalDateTime timestamp, TransactionCondition status){
        this.correlationId = correlationId;
        this.description = description;
        this.createdAt = timestamp;
        this.status = status;
    }
}
