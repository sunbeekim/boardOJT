package com.example.board.domain.user.entity.register;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.common.interfaces.BehaviorRegistrarInterface;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.interfaces.AdminUserBehavior;
import com.example.board.domain.user.entity.interfaces.impl.AdminUserBehaviorImpl;

import org.springframework.stereotype.Component;

@Component
public class AdminUserBehaviorRegistrar implements BehaviorRegistrarInterface {

    @Override
    public void register(BehaviorFactory factory) {
        factory.registerBehavior(
                User.class,
                AdminUserBehavior.class,
                adminUser -> new AdminUserBehaviorImpl((User) adminUser));
    }
}