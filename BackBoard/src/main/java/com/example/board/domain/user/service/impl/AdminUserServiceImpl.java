package com.example.board.domain.user.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.board.domain.user.dao.AdminMapper;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.service.AdminUserService;
import com.example.board.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    private User getUserOrThrow(Long userId, String errorMessage) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(errorMessage, "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND.value());
        }
        return user;
    }

    private void checkPermissionOrThrow(User user, String errorMessage) {
        if (!user.adminUserBehavior().checkPermission()) {
            throw new BusinessException(errorMessage, "권한이 부족합니다.", HttpStatus.FORBIDDEN.value());
        }
    }

    @Override
    public void deletePost(Long postId, Long adminId) {
        User admin = getUserOrThrow(adminId, "게시글 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "게시글 삭제에 실패했습니다.");
        adminMapper.postDelete(postId);
    }

    @Override
    public void deleteComment(Long commentId, Long adminId) {
        User admin = getUserOrThrow(adminId, "댓글 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "댓글 삭제에 실패했습니다.");
        adminMapper.commentDelete(commentId);
    }

    @Override
    public void deleteUser(Long targetId, Long adminId) {
        User admin = getUserOrThrow(adminId, "계정 삭제에 실패했습니다.");
        checkPermissionOrThrow(admin, "계정 삭제에 실패했습니다.");
        adminMapper.userDelete(targetId);
    }

    @Override
    public void unlockUser(Long targetId, Long adminId) {
        User admin = getUserOrThrow(adminId, "계정 잠금 해지에 실패했습니다.");
        checkPermissionOrThrow(admin, "계정 잠금 해지에 실패했습니다.");

        User targetUser = getUserOrThrow(targetId, "계정 잠금 해지에 실패했습니다.");
        targetUser.adminUserBehavior().unlock();
        adminMapper.unlocked(targetUser);
    }
}
