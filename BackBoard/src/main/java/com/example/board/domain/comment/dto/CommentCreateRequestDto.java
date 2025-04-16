package com.example.board.domain.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentCreateRequestDto {
    @NotBlank(message = "공백 또는 입력하지 않은 부분이 있습니다.")
    @Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하여야 합니다.")
    private String content;

    @NotBlank(message = "PostId는 필수 입력값입니다.")
    private Long postId;

    public CommentCreateRequestDto() {
    }

    public CommentCreateRequestDto(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }

}
