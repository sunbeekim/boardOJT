package com.example.board.domain.user.service.impl;

import javax.xml.stream.events.Comment;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.comment.dao.CommentMapper;
import com.example.board.domain.post.dao.PostMapper;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.user.dao.AdminMapper;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.service.AdminUserService;
import com.example.board.domain.user.validator.AdminValidator;

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
        User user = userMapper.findById(adminId);
        log.info("post ={}", user);

        AdminUserBehavior behavior = behaviorFactory.wrap(user, AdminUserBehavior.class);
        adminValidator.checkRole(behavior.checkPermission());
        adminValidator.getUserOrThrow(postId, "post", "게시글 삭제에 실패했습니다.");
        try {

            adminMapper.postDelete(postId);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    public void deleteComment(Long commentId, Long adminId) {
        User user = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(user, AdminUserBehavior.class);
        adminValidator.checkRole(behavior.checkPermission());
        adminValidator.getUserOrThrow(commentId, "comment", "댓글 삭제에 실패했습니다.");
        try {
            adminMapper.commentDelete(commentId);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    public void deleteUser(Long targetId, Long adminId) {
        User user = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(user, AdminUserBehavior.class);
        adminValidator.checkRole(behavior.checkPermission());
        adminValidator.getUserOrThrow(targetId, "user", "계정 삭제에 실패했습니다.");
        try {
            adminMapper.userDelete(targetId);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Override
    public void unlockUser(Long targetId, Long adminId) {
        User user = userMapper.findById(adminId);
        AdminUserBehavior behavior = behaviorFactory.wrap(user, AdminUserBehavior.class);
        adminValidator.checkRole(behavior.checkPermission());
        adminValidator.getUserOrThrow(targetId, "user", "계정 잠금 해지에 실패했습니다.");

        User targetUser = userMapper.findById(targetId);
        log.info("targetUser", targetUser);
        AdminUserBehavior targetBehavior = behaviorFactory.wrap(targetUser, AdminUserBehavior.class);
        targetBehavior.unlock();
        targetBehavior.resetLoginFailCount();

        try {
            adminMapper.unlocked(targetId);
        } catch (PersistenceException e) {
            throw e;
        }
    }
}
