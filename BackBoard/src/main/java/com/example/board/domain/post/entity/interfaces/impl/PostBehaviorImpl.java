package com.example.board.domain.post.entity.interfaces.impl;

import java.time.LocalDateTime;

import com.example.board.domain.post.entity.Post;
import com.example.board.domain.post.entity.interfaces.PostBehavior;
import com.example.board.exception.ForbiddenException;

public class PostBehaviorImpl implements PostBehavior {
    private final Post post;

    public PostBehaviorImpl(Post post) {
        this.post = post;
    }

    @Override
    public void changeTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수 입력값입니다.");
        }
        if (title.length() < 2 || title.length() > 100) {
            throw new IllegalArgumentException("제목은 2~100자 사이여야 합니다.");
        }
        post.setTitle(title);
    }

    @Override
    public void changeContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수 입력값입니다.");
        }
        if (content.length() < 2 || content.length() > 1000) {
            throw new IllegalArgumentException("내용은 2~1000자 사이여야 합니다.");
        }
        post.setContent(content);
    }

    @Override
    public void validateOwnership(Long userId) {
        if (!isOwnedBy(userId)) {
            throw new ForbiddenException("게시글 작성자만 수정/삭제할 수 있습니다.");
        }
    }

    @Override
    public boolean isOwnedBy(Long userId) {
        return post.getUserId().equals(userId);
    }

    @Override
    public void update(String title, String content) {
        changeTitle(title);
        changeContent(content);
    }

    @Override
    public void increaseView(Long id) {
        post.setViewCount(post.getViewCount() + 1);
    }

}
