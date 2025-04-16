package com.example.board.domain.user.entity.provider;

import org.springframework.stereotype.Component;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserBehaviorProvider {
    private final BehaviorFactory behaviorFactory;

    public AdminUserBehavior getFor(User user) {
        return behaviorFactory.wrap(user, AdminUserBehavior.class);
    }
}
