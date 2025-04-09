package com.example.board.domain.user.entity.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.common.interfaces.EntityBehavior;

public interface UserBehavior extends EntityBehavior {
    void register(String rawPassword, PasswordEncoder encoder);

    boolean verifyPassword(String rawPassword, PasswordEncoder encoder);

}