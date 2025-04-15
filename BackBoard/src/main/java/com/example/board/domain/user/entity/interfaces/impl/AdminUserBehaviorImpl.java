package com.example.board.domain.user.entity.interfaces.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.enums.UserRole;

public class AdminUserBehaviorImpl implements AdminUserBehavior {
    private final User user;

    public AdminUserBehaviorImpl(User user) {
        this.user = user;
    }

    @Override
    public void unlock() {
        user.setLocked(false);

    }

    @Override
    public void changeUserRole() {

    }

    @Override
    public void resetLoginFailCount() {
        user.setLoginFailCount(0);
    }

    @Override
    public void changeForcePassword(String rawPassword, PasswordEncoder encoder) {

    }

    @Override
    public boolean checkPermission() {
        return UserRole.ROLE_ADMIN.equals(user.getRole());
    }

}
