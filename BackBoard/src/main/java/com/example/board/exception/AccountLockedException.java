package com.example.board.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends BusinessException {
    public AccountLockedException(String message) {
        super(message, "UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}