package com.example.board.exception;

import org.springframework.http.HttpStatus;

public class TableNotFoundException extends BusinessException {
    public TableNotFoundException(String message) {
        super(message, "TABLE_NOT_FOUND", HttpStatus.SERVICE_UNAVAILABLE.value());
    }
}