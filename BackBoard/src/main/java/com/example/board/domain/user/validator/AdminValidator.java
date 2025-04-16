package com.example.board.domain.user.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.board.common.interfaces.DomainValidatorInterface;
import com.example.board.common.validator.CommonValidator;
import com.example.board.domain.comment.dao.CommentMapper;
import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.post.dao.PostMapper;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;
import com.example.board.exception.AccountLockedException;
import com.example.board.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminValidator extends CommonValidator implements
        DomainValidatorInterface.UpdateValidator<UserUpdateRequestDto, User>,
        DomainValidatorInterface.DeleteValidator {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Override
    public void validateUpdate(UserUpdateRequestDto request, User entity) {
        validateExistenceFilter(entity, User.class);
    }

    @Override
    public void validateDelete(Object request, Object entity) {
        validateExistenceFilter(entity, User.class);
    }

    public void checkRole(boolean isRole) {
        if (!isRole) {
            throw new BusinessException("권한이 부족합니다.", "AccessDeniedException", HttpStatus.LOCKED.value());
        }
    }

    public User getUserOrThrow(Long commonId, String type, String errorMessage) {
        switch (type) {
            case "post":
                Post post = postMapper.findById(commonId);
                log.info("post = {}", post);
                if (post == null) {
                    throw new BusinessException(errorMessage, "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND.value());
                }
                break;

            case "comment":
                Comment comment = commentMapper.findById(commonId);
                log.info("comment = {}", comment);
                if (comment == null) {
                    throw new BusinessException(errorMessage, "존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND.value());
                }
                break;

            case "user":
                User user = userMapper.findById(commonId);
                log.info("user = {}", user);
                if (user == null) {
                    throw new BusinessException(errorMessage, "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND.value());
                }
                return user;

            default:
                throw new BusinessException("알 수 없는 타입입니다: " + type, "INVALID_TYPE", HttpStatus.BAD_REQUEST.value());
        }

        return userMapper.findById(commonId); // fallback: user 정보 항상 필요
    }

}
