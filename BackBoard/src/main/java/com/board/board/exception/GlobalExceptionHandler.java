package com.board.board.exception;

import com.board.board.dto.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponseDto<?>> handleBusinessException(BusinessException e) {
        log.error("Business Exception: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(CommonResponseDto.error(e.getMessage(), e.getErrorCode()));
    }

    // 인증 예외 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponseDto<?>> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponseDto.error("인증에 실패했습니다.", "AUTHENTICATION_FAILED"));
    }

    // 권한 예외 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponseDto<?>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access Denied Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(CommonResponseDto.error("접근 권한이 없습니다.", "ACCESS_DENIED"));
    }

    // 유효성 검사 예외 처리 (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("Validation Exception: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponseDto.error(errorMessage, "VALIDATION_FAILED"));
    }

    // 유효성 검사 예외 처리 (BindException)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponseDto<?>> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("Binding Exception: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponseDto.error(errorMessage, "BINDING_FAILED"));
    }

    // 유효성 검사 예외 처리 (ConstraintViolationException)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponseDto<?>> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        
        log.error("Constraint Violation: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponseDto.error(errorMessage, "CONSTRAINT_VIOLATION"));
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto<?>> handleException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDto.error("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", "INTERNAL_SERVER_ERROR"));
    }
} 