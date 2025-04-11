package com.example.board.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.exception.UnauthorizedException;

@Aspect
@Component
public class AuthValidationAspect {
    @Before("@annotation(com.example.board.common.annotation.RequireAuth)")
    public void validate(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof JwtUserInfo) {
                JwtUserInfo jwt = (JwtUserInfo) arg;
                if (jwt.getId() == null) {
                    throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
                }
            }
        }
    }
}
