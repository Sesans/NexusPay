package com.nexuspay.auth.infra;

import com.nexuspay.auth.application.dto.UserRegisteredEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class EmailListener {
    private final JavaMailSender mailSender;

    public EmailListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @EventListener
    public void sendOTPEmail(UserRegisteredEvent event){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("NexusPay");
            helper.setTo(event.email());
            helper.setText(event.otpCode(), true);
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}