package com.example.board.domain.user.service.impl;

import org.springframework.stereotype.Service;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.dao.AdminMapper;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.service.AdminUserService;
import com.example.board.domain.user.validator.AdminValidator;
import com.example.board.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final BehaviorFactory behaviorFactory;
    private final AdminValidator adminValidator;

    @Override
    public void deletePost(Long postId, Long adminId) {
        log.info("관리자 계정으로 게시글 삭제 요청 - postId: {}, adminId: {}", postId, adminId);
        
        // 관리자 권한 검증
        User admin = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(admin, AdminUserBehavior.class);
        if (!behavior.checkPermission()) {
            log.warn("관리자 권한 없음 - adminId: {}", adminId);
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        
        // 게시글 존재 여부 검증
        adminValidator.getUserOrThrow(postId, "post", "게시글 삭제에 실패했습니다.");
        
        adminMapper.postDelete(postId);
        log.info("관리자 게시글 삭제 완료 - postId: {}", postId);
    }

    @Override
    public void deleteComment(Long commentId, Long adminId) {
        log.info("관리자 계정으로 댓글 삭제 요청 - commentId: {}, adminId: {}", commentId, adminId);
        
        // 관리자 권한 검증
        User admin = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(admin, AdminUserBehavior.class);
        if (!behavior.checkPermission()) {
            log.warn("관리자 권한 없음 - adminId: {}", adminId);
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        
        // 댓글 존재 여부 검증
        adminValidator.getUserOrThrow(commentId, "comment", "댓글 삭제에 실패했습니다.");
        
        adminMapper.commentDelete(commentId);
        log.info("관리자 계정으로 댓글 삭제 완료 - commentId: {}", commentId);
    }

    @Override
    public void deleteUser(Long targetId, Long adminId) {
        log.info("관리자 계정으로 회원 삭제 요청 - targetId: {}, adminId: {}", targetId, adminId);
        
        // 관리자 권한 검증
        User admin = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(admin, AdminUserBehavior.class);
        if (!behavior.checkPermission()) {
            log.warn("관리자 권한 없음 - adminId: {}", adminId);
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        
        // 대상 회원 존재 여부 검증
        adminValidator.getUserOrThrow(targetId, "user", "계정 삭제에 실패했습니다.");
        
        adminMapper.userDelete(targetId);
        log.info("관리자 계정으로 회원 삭제 완료 - targetId: {}", targetId);
    }

    @Override
    public void unlockUser(Long targetId, Long adminId) {
        log.info("잠금 해제 요청 - targetId: {}, adminId: {}", targetId, adminId);
        
        // 관리자 권한 검증
        User admin = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(admin, AdminUserBehavior.class);
        if (!behavior.checkPermission()) {
            log.warn("관리자 권한 없음 - adminId: {}", adminId);
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        
        // 대상 회원 존재 여부 검증
        User targetUser = adminValidator.getUserOrThrow(targetId, "user", "계정 잠금 해제에 실패했습니다.");
        
        // 계정 잠금 해제 및 로그인 실패 횟수 초기화
        AdminUserBehavior targetBehavior = behaviorFactory.wrap(targetUser, AdminUserBehavior.class);
        targetBehavior.unlock();
        targetBehavior.resetLoginFailCount();
        
        adminMapper.unlocked(targetId);
        log.info("잠금 해제 완료 - targetId: {}", targetId);
    }
}
