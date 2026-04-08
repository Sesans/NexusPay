package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.UserLoginDTO;
import com.nexuspay.auth.domain.exception.InvalidCredentialsException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
    @Mock
    TokenService tokenService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    LoginUseCase useCase;
    UserLoginDTO dto;
    User user;

    @BeforeEach
    void setup(){
        dto = new UserLoginDTO("12312312312", "testPassword");
        user = new User("test", dto.cpf(), "test@gmail.com", 20, dto.password(), "123456", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should login the user successfully")
    void loginSuccess(){
        when(userRepository.findByCpf(dto.cpf())).thenReturn(Optional.ofNullable(user));
        when(tokenService.generateToken(user)).thenReturn("TokenJWT");
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);

        var response = useCase.execute(dto);

        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getStatus(), response.status());
        assertNotNull(response.token());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void loginUserNotFound() {
        when(userRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(dto));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is invalid")
    void loginInvalidPassword() {
        when(userRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(dto.password(), user.getPassword()))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(dto));
    }
}