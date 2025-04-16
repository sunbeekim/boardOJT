package com.example.board.domain.post.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PostUpdateRequestDto {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Size(min = 1, max = 5000, message = "내용은 1자 이상 5000자 이하여야 합니다.")
    private String content;

    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
