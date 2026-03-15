package com.nexuspay.auth.infra;

import com.nexuspay.auth.application.dto.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {
    @Async
    @EventListener
    public void sendOTPEmail(UserRegisteredEvent event){
        System.out.println(event.otpCode());
    }
}