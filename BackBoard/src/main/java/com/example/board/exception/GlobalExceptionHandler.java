package com.example.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.board.common.dto.CommonResponseDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        // 커스텀 비즈니스 예외 처리 - 비지니스 예외 슈퍼클래스(비즈니스 예외들이 여기서 처리 됨)
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<CommonResponseDto<?>> handleBusinessException(BusinessException e) {
                log.error("Business Exception: {}", e.getMessage());
                return ResponseEntity
                                .status(e.getStatus())
                                .body(CommonResponseDto.error(e.getMessage(), e.getErrorCode(), e.getStatus()));
        }

        // 인증 예외 처리 - 인증 실패 시 발생 401
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<CommonResponseDto<?>> handleAuthenticationException(AuthenticationException e) {
                log.error("Authentication Exception: {}", e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(CommonResponseDto.error("인증에 실패했습니다.", "AUTHENTICATION_FAILED", 401));
        }

        // 권한 예외 처리 - 인증 성공이나 권한 부족인 경우 403
        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<CommonResponseDto<?>> handleAccessDeniedException(AccessDeniedException e) {
                log.error("Access Denied Exception: {}", e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(CommonResponseDto.error("접근 권한이 없습니다. [보안설정 확인]", "ACCESS_DENIED", 403));
        }

        // 유효성 검사 예외 처리 (MethodArgumentNotValidException) - dto RequestBody에 대한 매치 실패(필수
        // 값 누락 or 불필요 값 포함), 400 Bad Request
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<CommonResponseDto<?>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException e) {
                List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
                String errorMessage = fieldErrors.stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                log.error("Validation Exception: {}", errorMessage);
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(CommonResponseDto.error(errorMessage, "VALIDATION_FAILED",
                                                "400 - DTO 조건 불충족족(요청 바디 검증 실패)"));
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
                                .body(CommonResponseDto.error(errorMessage, "BINDING_FAILED", "400 - 폼 필드 값 검증 실패"));
        }

        // 유효성 검사 예외 처리 (ConstraintViolationException) - Valid, 사이즈, 패턴 등 실패 시 발생
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<CommonResponseDto<?>> handleConstraintViolationException(ConstraintViolationException e) {
                Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
                String errorMessage = violations.stream()
                                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                                .collect(Collectors.joining(", "));

                log.error("Constraint Violation: {}", errorMessage);
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(CommonResponseDto.error(errorMessage, "CONSTRAINT_VIOLATION",
                                                "400 - @Pattern, @Min(1), @NotBlank 등"));
        }

        @ExceptionHandler(TableNotFoundException.class)
        public ResponseEntity<CommonResponseDto<?>> handleTableNotFound(TableNotFoundException e) {
                log.error("Table Not Found: {}", e.getMessage());
                return ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(CommonResponseDto.error(e.getMessage(), e.getErrorCode()));
        }
}