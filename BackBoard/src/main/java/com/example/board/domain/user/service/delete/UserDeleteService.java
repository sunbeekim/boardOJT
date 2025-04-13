package com.example.board.service.delete;

public interface UserDeleteService {
    String getRole(); // "MIDDLEADMIN", "ADMIN"
    void delete(Long targetUserId, Long requesterUserId);
}