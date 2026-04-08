package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.VerifyOTP;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.model.UserStatus;
import com.nexuspay.auth.domain.model.VerificationCode;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserVerificationUseCaseTest {
    @Mock
    UserRepository userRepository;
    @Mock
    VerificationCodeRepository codeRepository;
    @InjectMocks
    UserVerificationUseCase useCase;
    User user;
    VerifyOTP dto;
    VerificationCode verificationCode;
    UUID userId = UUID.randomUUID();

    @BeforeEach
    void setup(){
        user = new User("test", "12312312312", "test@gmail.com", 20, "testPassword", "123456", LocalDateTime.now());
        verificationCode = new VerificationCode("123456", userId);
        dto = new VerifyOTP("123456");
    }

    @Test
    @DisplayName("Should verify the OTP and set the status to VERIFIED")
    void verificationSuccess(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(codeRepository.findByUserId(userId)).thenReturn(Optional.of(verificationCode));

        useCase.execute(userId, dto);

        assertEquals(UserStatus.VERIFIED, user.getStatus());
    }
}