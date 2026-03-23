package com.nexuspay.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserLoginDTO(
        @CPF
        @NotBlank(message = "The user CPF cannot be blank!")
        String cpf,
        String password
) {
}
