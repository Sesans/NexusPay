package com.nexuspay.auth.application;

import com.nexuspay.auth.UserFacade;
import com.nexuspay.auth.application.dto.UserDTO;
import com.nexuspay.auth.domain.exception.UserNotFoundException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserFacadeImpl implements UserFacade {
    private final UserRepository userRepository;

    public UserFacadeImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return new UserDTO(
                id,
                user.getName(),
                user.getStatus()
        );
    }
}
