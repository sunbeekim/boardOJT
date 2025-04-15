package com.example.board.common.validator;

import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.entity.User;
import com.example.board.exception.ResourceNotFoundException;

public class CommonValidator {

    public void validateExistenceFilter(Object entity) {
        if (entity instanceof User) {
            throw new ResourceNotFoundException("요청한 이메일이 존재하지 않습니다.");
        } else if (entity instanceof Post) {
            throw new ResourceNotFoundException("요청한 게시글이 존재하지 않습니다.");
        } else if (entity instanceof Comment) {
            throw new ResourceNotFoundException("요청한 댓글이 존재하지 않습니다.");
        }
    }
}
