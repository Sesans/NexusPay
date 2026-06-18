package com.nexuspay.auth.application;

import com.nexuspay.auth.domain.exception.UserNotFoundException;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserFacadeImpl facade;

    UUID userId;

    @BeforeEach
    void setup(){
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should retrieve the user data successfully")
    void getUserSuccess(){
        User user = new User("test", "12324434398", "test@gmail.com", 20, "test@mao", "123456", LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var response = facade.getUserById(userId);

        assertEquals(userId, response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getStatus(), response.status());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to find user")
    void getUserException(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> facade.getUserById(userId));

        verify(userRepository, times(1)).findById(any());
    }
}