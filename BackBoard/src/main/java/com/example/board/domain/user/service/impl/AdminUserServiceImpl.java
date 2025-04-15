package com.example.board.domain.user.service.impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.comment.dao.CommentMapper;
import com.example.board.domain.comment.entity.Comment;
import com.example.board.domain.post.dao.PostMapper;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.dao.AdminMapper;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.service.AdminUserService;
import com.example.board.domain.user.validator.AdminValidator;
import com.example.board.domain.user.validator.UserValidator;
import com.example.board.exception.BusinessException;
import com.example.board.exception.TableNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final BehaviorFactory behaviorFactory;
    private final AdminValidator adminValidator;

    private User getUserOrThrow(Long userId, Long commonId, String type, String errorMessage) {
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
                User user = userMapper.findById(userId);
                log.info("user = {}", user);
                if (user == null) {
                    throw new BusinessException(errorMessage, "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND.value());
                }
                return user;

            default:
                throw new BusinessException("알 수 없는 타입입니다: " + type, "INVALID_TYPE", HttpStatus.BAD_REQUEST.value());
        }

        return userMapper.findById(userId); // fallback: user 정보 항상 필요
    }

    private void checkPermissionOrThrow(User user, String errorMessage) {
        AdminUserBehavior behavior = behaviorFactory.wrap(user, AdminUserBehavior.class);
        if (!behavior.checkPermission()) {
            throw new BusinessException(errorMessage, "권한이 부족합니다.", HttpStatus.FORBIDDEN.value());
        }
    }

    @Override
    public void deletePost(Long postId, Long adminId) {
        User admin = getUserOrThrow(adminId, postId, "post", "게시글 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "게시글 삭제에 실패했습니다.");

        try {
            adminValidator.validateDelete(postId, Post.class);
            adminMapper.postDelete(postId);
        } catch (PersistenceException e) {
            if (e.getMessage().contains("doesn't exist") || e.getMessage().contains("unknown column")) {
                throw new TableNotFoundException("리소스가 존재하지 않습니다: " + e.getMessage());
            }
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void deleteComment(Long commentId, Long adminId) {
        User admin = getUserOrThrow(adminId, commentId, "comment", "댓글 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "댓글 삭제에 실패했습니다.");

        try {
            adminMapper.commentDelete(commentId);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void deleteUser(Long targetId, Long adminId) {
        User admin = getUserOrThrow(adminId, targetId, "user", "계정 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "계정 삭제에 실패했습니다.");

        try {
            adminMapper.userDelete(targetId);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void unlockUser(Long targetId, Long adminId) {
        User admin = getUserOrThrow(adminId, targetId, "user", "계정 잠금 해지에 실패했습니다.");
        checkPermissionOrThrow(admin, "계정 잠금 해지에 실패했습니다.");

        User targetUser = getUserOrThrow(targetId, targetId, "user", "계정 잠금 해지에 실패했습니다.");
        AdminUserBehavior behavior = behaviorFactory.wrap(targetUser, AdminUserBehavior.class);
        behavior.unlock();

        try {
            adminMapper.unlocked(targetUser);
        } catch (PersistenceException e) {
            throw new BusinessException("데이터베이스 오류가 발생했습니다.", "DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
