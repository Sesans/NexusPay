package com.nexuspay.auth.internal.infra;

import com.nexuspay.auth.internal.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
