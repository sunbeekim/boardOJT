package com.example.board.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentRequestDto{

    @NotBlank(message = "공백 또는 입력하지 않은 부분이 있습니다.")
    @Size(max = 1000, message = "최대입력 사이즈는 100자 입니다.")
    private String content;

}