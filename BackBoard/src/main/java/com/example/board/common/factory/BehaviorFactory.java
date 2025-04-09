package com.example.board.common.factory;

import org.springframework.stereotype.Component;

import com.example.board.common.interfaces.EntityBehavior;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.UserBehaviorImpl;
import com.example.board.domain.user.entity.interfaces.UserBehavior;

@Component
public class BehaviorFactory {
    public <T extends EntityBehavior> T wrap(Object entity, Class<T> type) {
        if (type == UserBehavior.class && entity instanceof User) {
            return type.cast(new UserBehaviorImpl((User) entity));
        }
        throw new IllegalArgumentException("정의되지 않은 타입");
    }
}
