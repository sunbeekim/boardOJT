package com.example.board.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(message, "ACCOUNT_DENIED", HttpStatus.LOCKED.value());
    }
} 