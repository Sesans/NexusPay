package com.nexuspay.auth;

import com.nexuspay.auth.application.dto.UserDTO;

import java.util.UUID;

public interface UserFacade {
    UserDTO getUserById(UUID id);
}
