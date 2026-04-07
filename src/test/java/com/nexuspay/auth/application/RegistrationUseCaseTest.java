package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.UserRequestDTO;
import com.nexuspay.auth.application.dto.UserResponseDTO;
import com.nexuspay.auth.domain.exception.DuplicateUserException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.model.UserStatus;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import com.nexuspay.auth.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationUseCaseTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    TokenService tokenService;
    @Mock
    VerificationCodeRepository codeRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @InjectMocks
    UserRegistrationUseCase authService;
    UserRequestDTO dto;

    @BeforeEach
    void setup(){
        dto = new UserRequestDTO("Test", "12312312312", "test@gmail.com", 18, "passwordTest", "transactionTest");
    }


    @Test
    @DisplayName("Should register a user successfully")
    void registerUser(){
        when(userRepository.existsByCpf(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(tokenService.generateToken(any(User.class))).thenReturn("testToken");

        UserResponseDTO response = authService.execute(dto);

        assertNotNull(response);
        assertEquals(dto.name(), response.name());
        assertEquals(dto.email(), response.email());
        assertEquals(UserStatus.PENDING, response.status());
        assertNotNull(response.token());
    }

    @Test
    @DisplayName("Should throw DuplicateUserException when trying to use a duplicated CPF")
    void registerUserException1(){
        when(userRepository.existsByCpf(anyString())).thenReturn(true);

        DuplicateUserException ex = assertThrows(DuplicateUserException.class,
                () -> authService.execute(dto));

        assertEquals("CPF already registered!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw DuplicateUserException when trying to use a duplicated Email")
    void registerUserException2(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateUserException ex = assertThrows(DuplicateUserException.class,
                () -> authService.execute(dto));

        assertEquals("Email already registered!", ex.getMessage());
    }
}