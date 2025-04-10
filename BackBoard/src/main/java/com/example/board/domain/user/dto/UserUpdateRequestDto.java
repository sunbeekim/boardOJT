package com.example.board.domain.user.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateRequestDto {

    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 알파벳, 숫자만 사용 가능합니다.")
    private String nickname;

    public UserUpdateRequestDto(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }
}
