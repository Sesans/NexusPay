package com.nexuspay.auth.infra;

import com.nexuspay.auth.application.dto.UserRegisteredEvent;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class SqsEventBridge {
    private final SqsTemplate sqsTemplate;
    @Value("${events.queues.user-registration}")
    private String queueUrl;

    public SqsEventBridge(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @ApplicationModuleListener
    public void sendOTPEmail(UserRegisteredEvent event){
        sqsTemplate.send(queueUrl, event);
    }
}