package com.nexuspay.auth.domain.model;

import com.nexuspay.auth.domain.exception.ExpiredCodeException;
import com.nexuspay.auth.domain.exception.InvalidCodeException;
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

    private boolean isExpired(){
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void validate(String inputCode){
        if(this.isExpired()) throw new ExpiredCodeException("This verification code is expired!");
        if(!this.code.equals(inputCode)) throw new InvalidCodeException("The code " + inputCode + " doesn't match! " +
                "Try again with the correct code.");
    }
}
