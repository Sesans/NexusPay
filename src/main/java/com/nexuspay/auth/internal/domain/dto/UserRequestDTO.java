package com.nexuspay.auth.internal.domain.dto;

public record UserRequestDTO(
        String name,
        String cpf,
        String email,
        int age,
        String password,
        String transactionPin
) {
}
