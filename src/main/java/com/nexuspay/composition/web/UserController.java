package com.nexuspay.composition.web;

import com.nexuspay.composition.application.UserService;
import com.nexuspay.composition.application.dto.UserSummaryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('VERIFIED')")
    public UserSummaryDTO getUserSummary(@AuthenticationPrincipal UUID userID){
        return userService.getUserSummary(userID);
    }
}
