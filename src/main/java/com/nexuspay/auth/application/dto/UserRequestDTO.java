package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDTO(
        @NotBlank(message = "The user name cannot be blank!")
        @Length(max = 40, message = "Length must be between 1 and 40!")
        String name,
        @NotBlank(message = "The user CPF cannot be blank!")
        @CPF(message = "Invalid CPF!")
        String cpf,
        @NotNull(message = "The user email cannot be null!")
        @Email(message = "Invalid E-mail!")
        @Length(max = 60)
        String email,
        @NotNull(message = "The user age cannot be null!")
        @Positive(message = "Must be a positive number!")
        int age,
        @NotBlank(message = "The user password cannot be blank!")
        @Length(max = 64, message = "The password must be no more than 64 characters long")
        String password,
        @NotBlank(message = "The user transaction pin cannot be blank!")
        @Length(max = 6, message = "The Transaction Pin must be no more than 6 characters long")
        String transactionPin
) {
}
