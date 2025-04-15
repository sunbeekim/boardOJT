package com.example.board.domain.user.validator;

import org.springframework.stereotype.Component;

import com.example.board.common.interfaces.DomainValidatorInterface;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;
import com.example.board.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminValidator implements
        DomainValidatorInterface.UpdateValidator<UserUpdateRequestDto, User>,
        DomainValidatorInterface.DeleteValidator {

    private final UserMapper userMapper;

    @Override
    public void validateUpdate(UserUpdateRequestDto request, User entity) {

    }

    @Override
    public void validateDelete(Object request, Object entity) {
        if (userMapper.findById(request) == null) {
            throw new ResourceNotFoundException("요청한 리소스가 존재하지 않습니다.");
        }
    }

}
