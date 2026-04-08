package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.VerifyOTP;
import com.nexuspay.auth.domain.exception.ExpiredCodeException;
import com.nexuspay.auth.domain.exception.UserNotFoundException;
import com.nexuspay.auth.domain.exception.VerificationCodeNotFoundException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.model.VerificationCode;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserVerificationUseCase {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationRepository;

    public UserVerificationUseCase(UserRepository userRepository, VerificationCodeRepository verificationRepository) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void execute(UUID userId, VerifyOTP dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        VerificationCode code = verificationRepository.findByUserId(userId).orElseThrow(VerificationCodeNotFoundException::new);

        try{
            code.validate(dto.otp());
            user.setVerified();
            verificationRepository.delete(code);
        }catch (ExpiredCodeException ex){
            verificationRepository.delete(code);
            throw ex;
        }
    }
}
