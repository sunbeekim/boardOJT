package com.example.board.domain.user.validator;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.board.common.interfaces.DomainValidatorInterface;
import com.example.board.common.validator.CommonValidator;
import com.example.board.exception.AccountLockedException;
import com.example.board.exception.BusinessException;
import com.example.board.exception.DuplicateResourceException;
import com.example.board.exception.ResourceNotFoundException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.LoginRequestDto;
import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.domain.user.dto.UserUpdateRequestDto;
import com.example.board.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 엔티티의 상태변화에 관여하지 않고 오직 검증만만
// 외부접근 통한 검증 or 유저 엔티티 필수 검증이 아닌 것것
@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator extends CommonValidator implements
        DomainValidatorInterface.CreateValidator<SignUpRequestDto, User>,
        DomainValidatorInterface.UpdateValidator<UserUpdateRequestDto, User>,
        DomainValidatorInterface.DeleteValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]+$");

    private final UserMapper userMapper;

    // 검증 통합 메서드 =============================================================//
    @Override
    public void validateDelete(Object id, Object entity) {
        validateExistenceFilter(entity);
        verifyAccount((User) entity);
    }

    @Override
    public void validateUpdate(UserUpdateRequestDto request, User currentUser) {
        validateExistenceFilter(currentUser);
        verifyAccount(currentUser);
        if (!request.getNickname().equals(currentUser.getNickname())) {
            // 현재 사용자의 닉네임이 아닌 경우에만 중복 검증
            validateNickname(currentUser.getNickname());
        }
        validatePasswordFormat(request.getPassword());
        validateNicknameFormat(request.getNickname());
    }

    @Override
    public void validateCreate(SignUpRequestDto request, User currentUser) {
        log.info("UserValidator request = {}", request);
        validateEmail(request.getEmail());
        validateNickname(request.getNickname());
        validateEmailFormat(request.getEmail());
        validatePasswordFormat(request.getPassword());
        validateNicknameFormat(request.getNickname());
    }
    // 유효성 검사 ===========================================================//

    public void validateEmailFormat(String email) {
        if (!StringUtils.hasText(email)) {
            throw new BusinessException("이메일은 필수 입력값입니다.", "EMAIL_REQUIRED", 400);
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("올바른 이메일 형식이 아닙니다.", "EMAIL_INVALID", 400);
        }
    }

    public void validatePasswordFormat(String password) {
        if (!StringUtils.hasText(password)) {
            throw new BusinessException("비밀번호는 필수 입력값입니다.", "PASSWORD_REQUIRED", 400);
        }
        if (password.length() < 8 || password.length() > 16) {
            throw new BusinessException("비밀번호는 8~16자 사이여야 합니다.", "PASSWORD_LENGTH_INVALID", 400);
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BusinessException("비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.", "PASSWORD_COMPLEXITY_INVALID", 400);
        }
    }

    public void validateNicknameFormat(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            throw new BusinessException("닉네임은 필수 입력값입니다.", "NICKNAME_REQUIRED", 400);
        }
        if (nickname.length() < 2 || nickname.length() > 10) {
            throw new BusinessException("닉네임은 2~10자 사이여야 합니다.", "NICKNAME_LENGTH_INVALID", 400);
        }
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new BusinessException("닉네임은 한글, 알파벳, 숫자만 사용 가능합니다.", "NICKNAME_FORMAT_INVALID", 400);
        }
    }

    // ========================================================================//

    public void validateLogin(LoginRequestDto request, PasswordEncoder encoder) {

        User user = userMapper.findByEmail(request.getEmail());
        String rawPassword = request.getPassword();
        verifyAccount(user);
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void verifyAccount(User user) {
        // 계정 잠금 상태 확인
        if (user.isLocked()) {
            throw new AccountLockedException("계정이 잠겼습니다.");
        }
    }

    public void validateEmail(String email) {
        User verifyEmail = userMapper.findByEmail(email);

        // 이메일 중복 검증
        if (verifyEmail != null) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
    }

    public void validateNickname(String nickname) {
        User verifyNickname = userMapper.findByNickname(nickname);
        // 닉네임 중복 검증
        if (verifyNickname != null) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }
    }

    // 200 반환 분리된 중복확인 ===============================================//
    public boolean validateBooleanEmail(User user) {

        // 이메일 중복 검증
        if (user == null) {
            return true;
        }
        return false;
    }

    public boolean validateBooleanNickname(User user) {
        // 닉네임 중복 검증
        if (user == null) {
            return true;
        }
        return false;
    }

}