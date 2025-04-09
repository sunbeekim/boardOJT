package com.example.board.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    private String errorPath;

    public static <T> CommonResponseDto<T> success(T data) {
        return CommonResponseDto.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> success(String message, T data) {
        return CommonResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message) {
        return CommonResponseDto.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message, String error) {
        return CommonResponseDto.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message, String error, T data) {
        return CommonResponseDto.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message, String error, T data, String errorPath) {
        return CommonResponseDto.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .data(data)
                .errorPath(errorPath)
                .build();
    }
}