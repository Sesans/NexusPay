package com.nexuspay.ledger.internal.domain.model;

import com.nexuspay.ledger.domain.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Getter
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false, updatable = false)
    private UUID accountId;

    @Column(name = "transaction_id", nullable = false, updatable = false)
    private Long transactionId;

    @Column(nullable = false, updatable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType entryType;

    @Column(nullable = false, updatable = false)
    private String currentHash;

    @Column(nullable = false, updatable = false)
    private String previousHash;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected LedgerEntry() {}

    public LedgerEntry(UUID accountId, Long transactionId, Long amount, TransactionType type, String previousHash, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.entryType = type;
        this.previousHash = previousHash;
        this.createdAt = timestamp;

        this.currentHash = calculateHash(previousHash, accountId, transactionId, amount, type, createdAt);
    }

    private String calculateHash(String prev, UUID accId, Long txId, Long amt, TransactionType type, LocalDateTime date) {
        try {
            String input = String.format("%s|%s|%d|%d|%s|%s", prev, accId, txId, amt, type, date);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
