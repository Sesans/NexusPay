package com.nexuspay.auth.application;

import com.nexuspay.auth.application.dto.UserRegisteredEvent;
import com.nexuspay.auth.application.dto.UserResponseDTO;
import com.nexuspay.auth.application.dto.VerifyOTP;
import com.nexuspay.auth.domain.model.VerificationCode;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.application.dto.UserRequestDTO;
import com.nexuspay.auth.domain.exception.DuplicateUserException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import com.nexuspay.auth.infra.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final VerificationCodeRepository verificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, TokenService tokenService, VerificationCodeRepository verificationRepository, ApplicationEventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.verificationRepository = verificationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserResponseDTO register(UserRequestDTO dto){
        if(userRepository.existsByCpf(dto.cpf()))
            throw new DuplicateUserException("CPF already registered!");
        if(userRepository.existsByEmail(dto.email()))
            throw new DuplicateUserException("Email already registered!");

        String password = passwordEncoder.encode(dto.password());
        String transactionPin = passwordEncoder.encode(dto.transactionPin());
        LocalDateTime timestamp = LocalDateTime.now();

        User user = new User(dto.name(), dto.cpf(), dto.email(), dto.age(), password, transactionPin, timestamp);
        userRepository.save(user);

        String otp = generateOTP();
        VerificationCode codeEntity = new VerificationCode(otp, user.getId());
        verificationRepository.save(codeEntity);
        eventPublisher.publishEvent(new UserRegisteredEvent(user.getEmail(), otp));

        String token = tokenService.generateToken(user);
        return new UserResponseDTO(
                dto.name(),
                dto.email(),
                user.getStatus(),
                timestamp,
                token
        );
    }

    private String generateOTP() {
        return SECURE_RANDOM.ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    @Transactional
    public void verify(VerifyOTP dto) {
        VerificationCode code = verificationRepository.findByUserId(UUID.randomUUID()).orElseThrow();
        code.validate(dto.otp());
        User user = verificationRepository.findUserByUserId(code.getUserId()).orElseThrow();
        user.setVerified();
    }

}
