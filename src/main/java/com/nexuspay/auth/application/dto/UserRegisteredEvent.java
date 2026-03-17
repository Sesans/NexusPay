package com.nexuspay.auth.application.dto;

public record UserRegisteredEvent(
        String email,
        String otpCode
) {
}