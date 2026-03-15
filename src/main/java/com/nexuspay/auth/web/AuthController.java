package com.nexuspay.auth.web;

import com.nexuspay.auth.application.AuthService;
import com.nexuspay.auth.application.dto.UserRequestDTO;
import com.nexuspay.auth.application.dto.UserResponseDTO;
import com.nexuspay.auth.application.dto.VerifyOTP;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO dto){
        return authService.register(dto);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@Valid VerifyOTP dto){
        authService.verify(dto);
    }
}
