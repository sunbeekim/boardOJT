package com.example.board.common.factory;

import com.example.board.common.interfaces.BehaviorRegistrarInterface;
import com.example.board.common.interfaces.EntityBehaviorInterface;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class BehaviorFactory {
    // 엔티티 타입, 행동 인터페이스 타입, Function(엔티티 -> 행동 인터페이스 구현체) key : entity type value : 람다식으로 구현된 행동 인터페이스 구현체체
    private final Map<Class<?>, Function<Object, EntityBehaviorInterface>> behaviorMap;

    public BehaviorFactory(List<BehaviorRegistrarInterface> registrars) {
        this.behaviorMap = new HashMap<>();
        registrars.forEach(registrar -> registrar.register(this));
    }

    public <E, B extends EntityBehaviorInterface> void registerBehavior(
            Class<E> entityType,
            Class<B> behaviorType,
            Function<E, B> creator) {

        // 위의 주석과 같이 creator는 Object -> EntityBehavior로 캐스팅 가능한 함수로 변환하여 저장
        behaviorMap.put(behaviorType, entity -> creator.apply(entityType.cast(entity)));
    }

    // registerBehavior를 통해 등록된 타입별 구현체를 사용하기 위한 메서드드
    public <T extends EntityBehaviorInterface> T wrap(Object entity, Class<T> type) { // EntityBehaviorInterface의 하위
                                                                                      // 인터페이스만 타입지정 가능
        // behaviorMap에서 해당 타입의 생성 함수를 조회
        Function<Object, EntityBehaviorInterface> creator = behaviorMap.get(type);
        if (creator == null) {
            // 등록되지 않은 타입이면 예외 발생
            throw new IllegalArgumentException(
                    String.format("Behavior type %s is not registered", type.getName()));
        }
        // 해당 생성 함수를 실행하여 구현체 반환
        return type.cast(creator.apply(entity));
    }
}
