package com.example.board.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class CheckRequestDto {

    // @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message =
    // "올바른 이메일 형식이 아닙니다.")
    private String email;

    // @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    // @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 알파벳, 숫자만 사용
    // 가능합니다.")
    private String nickname;

    public CheckRequestDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}