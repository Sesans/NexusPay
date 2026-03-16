package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyOTP(
        @NotBlank
        String otp
) {
}
