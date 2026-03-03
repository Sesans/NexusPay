package com.nexuspay.auth.internal.domain.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message){super(message);}
}
