package com.example.board.domain.user.registrar;

import org.springframework.stereotype.Component;
import com.example.board.common.factory.BehaviorFactory;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.UserBehaviorImpl;
import com.example.board.domain.user.entity.interfaces.UserBehavior;

@Component
public class UserBehaviorRegistrar implements BehaviorRegistrar {
    @Override
    public void register(BehaviorFactory factory) {
        factory.registerBehavior(
            User.class, 
            UserBehavior.class, 
            user -> new UserBehaviorImpl((User) user)
        );
    }
}