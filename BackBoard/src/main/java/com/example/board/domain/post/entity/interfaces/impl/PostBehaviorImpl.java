package com.example.board.domain.post.entity.interfaces.impl;

import java.time.LocalDateTime;

import com.example.board.domain.post.entity.Post;
import com.example.board.domain.post.entity.interfaces.PostBehavior;

public class PostBehaviorImpl implements PostBehavior {
    private final Post post;

    public PostBehaviorImpl(Post post) {
        this.post = post;
    }

    @Override
    public void changeTitle(String title) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeTitle'");
    }

    @Override
    public void changeContent(String content) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeContent'");
    }

    @Override
    public void increaseView(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseView'");
    }

    @Override
    public boolean isDeleted() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDeleted'");
    }

    @Override
    public void validateOwnership(Long userId) {

    }

    @Override
    public boolean isOwnedBy(Long userId) {
        return true;
    }

    @Override
    public void update(String title, String content) {
        post.setTitle(title);
        post.setContent(content);
    }

}
