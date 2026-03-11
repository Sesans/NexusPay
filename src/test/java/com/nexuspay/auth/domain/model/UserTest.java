package com.nexuspay.auth.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;

    @Test
    @DisplayName("Should create the user successfully")
    void userCreation(){
        LocalDateTime timestamp = LocalDateTime.now();
        user = new User("Test", "12312312312", "test@gmail.com", 18, "passwordTest", "pinTest", timestamp);

        assertNotNull(user);
        assertEquals(UserStatus.PENDING, user.getStatus());
    }
}