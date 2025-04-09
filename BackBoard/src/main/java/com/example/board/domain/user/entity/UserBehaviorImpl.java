package com.example.board.domain.user.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.board.domain.user.entity.interfaces.UserBehavior;

public class UserBehaviorImpl implements UserBehavior {
    private final User user;

    public UserBehaviorImpl(User user) {
        this.user = user;
    }

    @Override
    public void register(String rawPassword, PasswordEncoder encoder) {
        String encoded = encoder.encode(rawPassword);
        user.setPassword(encoded);
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public boolean verifyPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, user.getPassword());
    }

}
