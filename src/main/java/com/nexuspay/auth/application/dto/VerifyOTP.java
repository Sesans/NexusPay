package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record VerifyOTP(
        @NotBlank
        @Length(max = 6, message = "Must be no longer than 6 characters!")
        String otp
) {
}
