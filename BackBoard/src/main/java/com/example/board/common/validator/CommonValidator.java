package com.example.board.common.validator;

import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.entity.User;
import com.example.board.exception.ResourceNotFoundException;

public class CommonValidator {
    // Class<?> type
    // type.equals
    @SuppressWarnings("unused")
    public void validateExistenceFilter(Object entity) {
        if (entity instanceof User) {
            if (entity == null) {
                throw new ResourceNotFoundException("요청한 이메일이 존재하지 않습니다.");
            }
        } else if (entity instanceof Post) {
            if (entity == null) {
                throw new ResourceNotFoundException("요청한 게시글이 존재하지 않습니다.");
            }
        } else if (entity instanceof Comment) {
            if (entity == null) {
                throw new ResourceNotFoundException("요청한 댓글이 존재하지 않습니다.");
            }
        }
    }
}
