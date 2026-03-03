package com.nexuspay.auth.internal.application;

import com.nexuspay.auth.internal.domain.dto.UserRequestDTO;
import com.nexuspay.auth.internal.domain.exception.DuplicateUserException;
import com.nexuspay.auth.internal.domain.model.User;
import com.nexuspay.auth.internal.infra.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(UserRequestDTO dto){
        if(userRepository.existsByCpf(dto.cpf()))
            throw new DuplicateUserException("CPF already registered!");
        if(userRepository.existsByEmail(dto.email()))
            throw new DuplicateUserException("Email already registered!");

        String password = passwordEncoder.encode(dto.password());
        String transactionPin = passwordEncoder.encode(dto.transactionPin());
        LocalDateTime timestamp = LocalDateTime.now();

        User user = new User(dto.name(), dto.cpf(), dto.email(), dto.age(), password, transactionPin, timestamp);
        userRepository.save(user);
    }
}
