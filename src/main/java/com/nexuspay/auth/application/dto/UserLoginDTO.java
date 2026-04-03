package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record UserLoginDTO(
        @NotBlank(message = "The user CPF cannot be blank!")
        @CPF(message = "Invalid CPF!")
        String cpf,
        @NotBlank(message = "The user password cannot be blank!")
        @Length(max = 64, message = "The password must be no more than 64 characters long")
        String password
) {
}
