package com.example.board.common.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.board.common.interfaces.EntityBehavior;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.entity.UserBehaviorImpl;
import com.example.board.domain.user.entity.interfaces.UserBehavior;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class BehaviorFactory {
    private final Map<Class<?>, Function<Object, EntityBehavior>> behaviorMap;
    
    @Autowired
    public BehaviorFactory(List<BehaviorRegistrar> registrars) {
        this.behaviorMap = new HashMap<>();
        registrars.forEach(registrar -> registrar.register(this));
    }
    
    public <E, B extends EntityBehavior> void registerBehavior(
        Class<E> entityType, 
        Class<B> behaviorType,
        Function<E, B> creator) {
        behaviorMap.put(behaviorType, entity -> creator.apply(entityType.cast(entity)));
    }
    
    public <T extends EntityBehavior> T wrap(Object entity, Class<T> type) {
        Function<Object, EntityBehavior> creator = behaviorMap.get(type);
        if (creator == null) {
            throw new IllegalArgumentException(
                String.format("Behavior type %s is not registered", type.getName())
            );
        }
        return type.cast(creator.apply(entity));
    }
}
