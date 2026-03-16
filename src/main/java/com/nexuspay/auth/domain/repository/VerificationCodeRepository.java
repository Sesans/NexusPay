package com.nexuspay.auth.domain.repository;

import com.nexuspay.auth.domain.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByUserId(UUID uuid);
}
