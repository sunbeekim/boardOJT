package com.example.board.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private boolean writtenByBe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}