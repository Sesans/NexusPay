package com.nexuspay.auth;

import com.nexuspay.auth.application.AuthService;
import com.nexuspay.auth.application.dto.VerifyOTP;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.model.VerificationCode;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import com.nexuspay.auth.infra.security.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class AuthDBITTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    VerificationCodeRepository verificationRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    AuthService authService;

    @Test
    void connectionEstablished(){
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Should receive the correct OTP and set User Status to VERIFIED")
    void setUserVerified(){
        VerifyOTP dto = new VerifyOTP("123456");
        User user = new User("TEST", "11111111111", "TEST@gmail.com", 25, "TESTPASSWORD", "TESTPIN", LocalDateTime.now());
        userRepository.save(user);
        VerificationCode verificationCode = new VerificationCode("123456", user.getId());
        verificationRepository.save(verificationCode);

        authService.verify(user.getId(), dto);
    }
}
