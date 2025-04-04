package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {
    private String status;
    private String message;
    private T data;

    public static <T> CommonResponseDto<T> success(T data) {
        return CommonResponseDto.<T>builder()
                .status("success")
                .message("성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> success(String message, T data) {
        return CommonResponseDto.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message) {
        return CommonResponseDto.<T>builder()
                .status("error")
                .message(message)
                .data(null)
                .build();
    }
} 