package com.example.board.domain.user.service;

public interface AdminUserService {
    void deletePost(Long postId, Long adminId);

    void deleteComment(Long commentId, Long adminId);

    void deleteUser(Long targetUserId, Long adminId);

    void unlockUser(Long targetUserId, Long adminId);

}
