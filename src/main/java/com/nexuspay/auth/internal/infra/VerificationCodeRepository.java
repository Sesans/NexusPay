package com.nexuspay.auth.internal.infra;

import com.nexuspay.auth.internal.domain.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
}
