package com.example.board.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends BusinessException {
    public AccountLockedException(String message) {
        super(message, "ACCOUNT_LOCKED", HttpStatus.LOCKED.value());
    }
} 