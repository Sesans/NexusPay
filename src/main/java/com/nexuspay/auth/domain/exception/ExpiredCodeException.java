package com.nexuspay.auth.domain.exception;

public class ExpiredCodeException extends RuntimeException {
    public ExpiredCodeException(String message){super(message);}
}
