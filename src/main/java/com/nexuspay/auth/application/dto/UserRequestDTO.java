package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserRequestDTO(
        @NotBlank(message = "The user name cannot be blank!")
        String name,
        @NotBlank(message = "The user CPF cannot be blank!")
        String cpf,
        @NotNull(message = "The user email cannot be null!")
        @Email
        String email,
        @NotNull(message = "The user age cannot be null!")
        @Positive
        int age,
        @NotBlank(message = "The user password cannot be blank!")
        String password,
        @NotBlank(message = "The user transaction pin cannot be blank!")
        String transactionPin
) {
}
