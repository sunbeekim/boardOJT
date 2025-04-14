package com.example.board.domain.post.entity.register;

import com.example.board.common.factory.BehaviorFactory;
import com.example.board.common.interfaces.BehaviorRegistrarInterface;
import com.example.board.domain.post.entity.Post;
import com.example.board.domain.post.entity.interfaces.PostBehavior;
import com.example.board.domain.post.entity.interfaces.impl.PostBehaviorImpl;

import org.springframework.stereotype.Component;

@Component
public class PostBehaviorRegistrar implements BehaviorRegistrarInterface {

    @Override
    public void register(BehaviorFactory factory) {
        factory.registerBehavior(
                Post.class,
                PostBehavior.class,
                post -> new PostBehaviorImpl((Post) post));
    }
}