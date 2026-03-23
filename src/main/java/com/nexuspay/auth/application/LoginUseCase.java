package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.UserLoginDTO;
import com.nexuspay.auth.application.dto.UserResponseDTO;
import com.nexuspay.auth.domain.exception.InvalidCredentialsException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.infra.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(TokenService tokenService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO execute(UserLoginDTO dto) {
        User user = userRepository.findByCpf(dto.cpf())
                .filter(u -> passwordEncoder.matches(dto.password(), u.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid CPF or password!"));

        String token = tokenService.generateToken(user);

        return new UserResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt(),
                token
        );
    }
}
