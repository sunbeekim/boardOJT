package com.example.board.domain.post.entity;

import java.time.LocalDateTime;

import com.example.board.domain.post.entity.interfaces.PostBehavior;
import com.example.board.domain.post.entity.interfaces.impl.PostBehaviorImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostBehavior postBehavior() {
        return new PostBehaviorImpl(this);
    }
}
