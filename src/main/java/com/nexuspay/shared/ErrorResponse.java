package com.nexuspay.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp,
        Map<String, Object> details
) {
    public ErrorResponse(String code, String message) {
        this(code, message, LocalDateTime.now(), null);
    }

    public ErrorResponse(String code, String message, Map<String, Object> details) {
        this(code, message, LocalDateTime.now(), details);
    }
}