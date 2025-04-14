package com.example.board.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostSearchConditionDto {

    private String title;

    private String nickname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateFrom;

    private String sort = "created_at";

    private String direction = "DESC";

    private int size = 10; // 기본값 10

    private Long userId;
}
