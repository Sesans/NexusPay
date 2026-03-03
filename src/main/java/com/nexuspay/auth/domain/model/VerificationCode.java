package com.nexuspay.auth.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "verification_codes")
@Getter
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected VerificationCode(){}

    public VerificationCode(String code, UUID userId){
        this.code = code;
        this.userId = userId;
        this.expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
