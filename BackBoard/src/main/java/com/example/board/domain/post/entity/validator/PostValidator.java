package com.example.board.domain.post.entity.validator;

import org.springframework.stereotype.Component;

import com.example.board.common.interfaces.DomainValidatorInterface;
import com.example.board.common.validator.CommonValidator;
import com.example.board.domain.post.dto.PostUpdateRequestDto;
import com.example.board.domain.post.entity.Post;
import com.example.board.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;;

@Component
@RequiredArgsConstructor
public class PostValidator extends CommonValidator implements
        DomainValidatorInterface.CreateValidator<Post, Long>,
        DomainValidatorInterface.UpdateValidator<PostUpdateRequestDto, Post>,
        DomainValidatorInterface.DeleteValidator {

    @Override
    public void validateUpdate(PostUpdateRequestDto request, Post entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateUpdate'");
    }

    @Override
    public void validateCreate(Post post, Long userId) {
        if (!post.postBehavior().isOwnedBy(userId)) {
            throw new ForbiddenException("존재하지 않는 작성자입니다.");
        }
    }

    @Override
    public void validateDelete(Object request, Object entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateDelete'");
    }

}
