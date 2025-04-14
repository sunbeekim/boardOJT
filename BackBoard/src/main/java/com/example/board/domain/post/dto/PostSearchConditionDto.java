package com.example.board.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostSearchConditionDto {

    private String title;

    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public LocalDateTime getDateFrom() {
        return date != null ? date.atStartOfDay() : null;
    }

    public LocalDateTime getDateTo() {
        return date != null ? date.plusDays(1).atStartOfDay() : null;
    }

    private String sort = "created_at";

    private String direction = "DESC";

    private Integer page = 1;
    private Integer size = 10;

    public void setPage(Integer page) {
        if (page != null && page > 0) {
            this.page = page;
        }
    }

    public void setSize(Integer size) {
        if (size != null && List.of(5, 10, 20, 30).contains(size)) {
            this.size = size;
        } else {
            this.size = 10; // 기본값
        }
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    private Long userId;
}
