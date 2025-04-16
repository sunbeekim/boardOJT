package com.example.board.domain.user.entity.provider;

import org.springframework.stereotype.Component;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.UserBehavior;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserBehaviorProvider {

    private final BehaviorFactory behaviorFactory;

    public UserBehavior get(User user) {
        return behaviorFactory.wrap(user, UserBehavior.class);
    }
}
