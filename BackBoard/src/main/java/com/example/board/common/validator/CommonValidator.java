package com.example.board.common.validator;

import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.entity.User;
import com.example.board.exception.ResourceNotFoundException;

public class CommonValidator {
    // Class<?> type
    // type.equals
    public void validateExistenceFilter(Object entity, Class<?> expectedType) {
        if (entity == null) {
            if (expectedType.equals(User.class)) {
                throw new ResourceNotFoundException("요청한 계정이 존재하지 않습니다.");
            } else if (expectedType.equals(Post.class)) {
                throw new ResourceNotFoundException("요청한 게시글글이 존재하지 않습니다.");
            } else if (expectedType.equals(Comment.class)) {
                throw new ResourceNotFoundException("요청한 댓글이 존재하지 않습니다.");
            }
        }
    }
}
