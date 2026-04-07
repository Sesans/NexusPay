package com.nexuspay.auth.web;

import com.nexuspay.auth.application.UserRegistrationUseCase;
import com.nexuspay.auth.application.LoginUseCase;
import com.nexuspay.auth.application.UserVerificationUseCase;
import com.nexuspay.auth.application.dto.UserLoginDTO;
import com.nexuspay.auth.application.dto.UserRequestDTO;
import com.nexuspay.auth.application.dto.UserResponseDTO;
import com.nexuspay.auth.application.dto.VerifyOTP;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRegistrationUseCase registrationUseCase;
    private final LoginUseCase loginUseCase;
    private final UserVerificationUseCase verificationUseCase;

    public AuthController(UserRegistrationUseCase registrationUseCase, LoginUseCase loginUseCase, UserVerificationUseCase verificationUseCase) {
        this.registrationUseCase = registrationUseCase;
        this.loginUseCase = loginUseCase;
        this.verificationUseCase = verificationUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO dto){
        return registrationUseCase.execute(dto);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@AuthenticationPrincipal UUID userId, @RequestBody @Valid VerifyOTP dto){
        verificationUseCase.execute(userId, dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO login(@RequestBody @Valid UserLoginDTO dto){
        return loginUseCase.execute(dto);
    }
}
