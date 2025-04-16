package com.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.board.util.JwtUtil;
import io.jsonwebtoken.JwtException;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.user.dao.UserMapper;
import com.example.board.domain.user.dto.*;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.enums.UserRole;
import com.example.board.domain.user.service.*;
import com.example.board.exception.DuplicateResourceException;
import com.example.board.exception.GlobalExceptionHandler;
import com.example.board.exception.UnauthorizedException;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

@SpringBootTest
@AutoConfigureMockMvc
// @WebMvcTest(UserController.class) 시큐리티 필터체인 비적용
@AutoConfigureRestDocs
@WithMockUser
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private UserMapper userMapper;

        @MockBean
        private User user;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private Authentication authentication;

        @MockBean
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ObjectMapper objectMapper;

        private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWQiOjExLCJuaWNrbmFtZSI6InRlc3QiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTc0NDcwMzY2MSwiZXhwIjoxNzQ0NzkwMDYxfQ.Pfpaw0_AFkV_4_peWl0CY1qegJBs_x-cqlIQLt_pFDs";

        @Test
        void checkEmailTest() throws Exception {
                CheckRequestDto request = new CheckRequestDto("test@email.com", "");
                when(userService.checkEmail(any(CheckRequestDto.class))).thenReturn(true);

                mockMvc.perform(post("/api/users/check/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("check-email",
                                                requestFields(
                                                                fieldWithPath("email").description("중복 확인할 이메일"),
                                                                fieldWithPath("nickname").description("중복 확인할 닉네임")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data")
                                                                                .description("사용 가능 여부 (true/false)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void checkNicknameTest() throws Exception {
                CheckRequestDto request = new CheckRequestDto("test@test.test", "test");
                when(userService.checkNickname(any(CheckRequestDto.class))).thenReturn(true);

                mockMvc.perform(post("/api/users/check/nickname")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("check-nickname",
                                                requestFields(
                                                                fieldWithPath("email").description("중복 확인할 이메일"),
                                                                fieldWithPath("nickname").description("중복 확인할 닉네임")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data")
                                                                                .description("사용 가능 여부 (true/false)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void signUpTest() throws Exception {
                SignUpRequestDto request = new SignUpRequestDto("test@email.com",
                                "password123!", "nickname");

                mockMvc.perform(post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andDo(document("signup",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일")
                                                                                .attributes(key("constraints")
                                                                                                .value("이메일 형식")),
                                                                fieldWithPath("password").description("비밀번호")
                                                                                .attributes(key("constraints").value(
                                                                                                "8~16자, 영문, 숫자, 특수문자 필수 포함")),
                                                                fieldWithPath("nickname").description("닉네임")
                                                                                .attributes(key("constraints").value(
                                                                                                "2~10자, 한글, 알파벳, 숫자만 사용 가능"))),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (200)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void signUpFailTest_dueToPasswordMissing() throws Exception {
                SignUpRequestDto request = new SignUpRequestDto("test@email.com", "",
                                "nickname");

                mockMvc.perform(post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andDo(document("signup-fail-validation",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일"),
                                                                fieldWithPath("password").description("비밀번호"),
                                                                fieldWithPath("nickname").description("닉네임")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (false)"),
                                                                fieldWithPath("message").description("Bad Request"),
                                                                fieldWithPath("data").description(
                                                                                "400 - DTO 조건 불충족(요청 바디 검증 실패)"),
                                                                fieldWithPath("error")
                                                                                .description("CONSTRAINT_VIOLATION"),
                                                                fieldWithPath("errorPath").description("/error/400"))));
        }

        @Test
        void signUpFailTest_dueToDuplicateEmail() throws Exception {
                SignUpRequestDto request = new SignUpRequestDto("test@email.com",
                                "password123!", "nickname");
                doThrow(new DuplicateResourceException("이미 사용 중인 이메일입니다."))
                                .when(userService).signup(any(SignUpRequestDto.class));

                mockMvc.perform(post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isConflict())
                                .andDo(document("signup-fail-duplicate",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일"),
                                                                fieldWithPath("password").description("비밀번호"),
                                                                fieldWithPath("nickname").description("닉네임")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (false)"),
                                                                fieldWithPath("message")
                                                                                .description("이미 사용 중인 이메일입니다."),
                                                                fieldWithPath("data").description("409 - 중복된 리소스"),
                                                                fieldWithPath("error")
                                                                                .description("DUPLICATE_RESOURCE"),
                                                                fieldWithPath("errorPath").description("/error/409"))));
        }

        @Test
        void deleteTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");
                Authentication auth = new TestingAuthenticationToken(mockUserInfo, null);
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);

                mockMvc.perform(delete("/api/users/me")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("delete-user",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));

                SecurityContextHolder.clearContext();
        }

        @Test
        void loginTest() throws Exception {
                LoginRequestDto request = new LoginRequestDto("test@email.com",
                                "password123!");
                when(userService.login(any(LoginRequestDto.class))).thenReturn("jwt.token.here");

                mockMvc.perform(post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("login",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일"),
                                                                fieldWithPath("password").description("비밀번호")
                                                                                .attributes(key("constraints").value(
                                                                                                "8~16자, 영문, 숫자, 특수문자 필수 포함"))),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("JWT 토큰"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void updateTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");
                Authentication auth = new TestingAuthenticationToken(mockUserInfo, null);
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
                UserUpdateRequestDto request = new UserUpdateRequestDto("NewPass123!", "NewNick");
                mockMvc.perform(patch("/api/users/me")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("update-user",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                requestFields(
                                                                // requestFields(fieldWithPath("data").description("data")

                                                                fieldWithPath("password").description("새 비밀번호")
                                                                                .attributes(key("constraints").value(
                                                                                                "8~16자, 영문, 숫자, 특수문자 필수 포함")),
                                                                fieldWithPath("nickname").description("새 닉네임")
                                                                                .attributes(key("constraints").value(
                                                                                                "2~10자, 한글, 알파벳, 숫자만 사용 가능"))),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답데이터").optional(),
                                                                fieldWithPath("data.password").description("변경된 비밀번호"),
                                                                fieldWithPath("data.nickname").description("변경된 닉네임"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
                SecurityContextHolder.clearContext();
        }

        @Test
        void loginFailTest_dueToInvalidCredentials() throws Exception {
                LoginRequestDto request = new LoginRequestDto("test@email.com",
                                "wrongpassword!1");
                when(userService.login(any(LoginRequestDto.class)))
                                .thenThrow(new UnauthorizedException("비밀번호가 일치하지 않습니다."));

                mockMvc.perform(post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isUnauthorized())
                                .andDo(document("login-fail-credentials",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일"),
                                                                fieldWithPath("password").description("비밀번호")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (false)"),
                                                                fieldWithPath("message")
                                                                                .description("비밀번호가 일치하지 않습니다."),
                                                                fieldWithPath("data").description("401 - 인증 실패"),
                                                                fieldWithPath("error").description("UNAUTHORIZED"),
                                                                fieldWithPath("errorPath").description("/error/401"))));
        }

        // @Test
        // void deleteFailTest_dueToInvalidToken() throws Exception {
        //         JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");
        //         Authentication auth = new TestingAuthenticationToken(mockUserInfo, null);
        //         auth.setAuthenticated(true);
        //         SecurityContextHolder.getContext().setAuthentication(auth);
        //         doThrow(new JwtException("Invalid token"))
        //                         .when(jwtUtil).getEmailFromToken(anyString());

        //         mockMvc.perform(delete("/api/users/me")
        //                         .requestAttr("userInfo", mockUserInfo)
        //                         .header("Authorization", "Bearer " + token + "t"))
        //                         .andExpect(status().isUnauthorized())
        //                         .andDo(document("delete-user-fail-token",
        //                                         requestHeaders(
        //                                                         headerWithName("Authorization")
        //                                                                         .description("잘못된 JWT 토큰")),
        //                                         responseFields(
        //                                                         fieldWithPath("success")
        //                                                                         .description("요청 성공 여부 (false)"),
        //                                                         fieldWithPath("message").description("Invalid token"),
        //                                                         fieldWithPath("data").description("401 - 인증 실패"),
        //                                                         fieldWithPath("error").description("UNAUTHORIZED"),
        //                                                         fieldWithPath("errorPath").description("/error/401"))));
        //         SecurityContextHolder.clearContext();
        // }

        // @Test
        // void updateFailTest_dueToInvalidToken() throws Exception {

        //         JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");
        //         Authentication auth = new TestingAuthenticationToken(mockUserInfo, null);
        //         auth.setAuthenticated(true);
        //         SecurityContextHolder.getContext().setAuthentication(auth);
        //         UserUpdateRequestDto request = new UserUpdateRequestDto("NewPass123!",
        //                         "NewNick");
        //         doThrow(new JwtException("Invalid token"))
        //                         .when(jwtUtil).getEmailFromToken(anyString());

        //         mockMvc.perform(patch("/api/users/me")
        //                         .header("Authorization", "Bearer invalid.token.here")
        //                         .requestAttr("userInfo", mockUserInfo)
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(new ObjectMapper().writeValueAsString(request)))
        //                         .andExpect(status().isUnauthorized())
        //                         .andDo(document("update-user-fail-token",
        //                                         requestHeaders(
        //                                                         headerWithName("Authorization")
        //                                                                         .description("잘못된 JWT 토큰")),
        //                                         requestFields(
        //                                                         fieldWithPath("password").description("새 비밀번호"),
        //                                                         fieldWithPath("nickname").description("새 닉네임")),
        //                                         responseFields(
        //                                                         fieldWithPath("success")
        //                                                                         .description("요청 성공 여부 (false)"),
        //                                                         fieldWithPath("message").description("Invalid token"),
        //                                                         fieldWithPath("data").description("401 - 인증 실패"),
        //                                                         fieldWithPath("error").description("UNAUTHORIZED"),
        //                                                         fieldWithPath("errorPath").description("/error/401"))));
        //         SecurityContextHolder.clearContext();
        // }

}

// 타입 예시 설명
// 단일 필드 fieldWithPath("email") 단순한 키 필드
// 중첩 객체 fieldWithPath("user.email") user 안의 email 필드
// 리스트 fieldWithPath("users[]") users라는 배열
// 리스트 내 객체 fieldWithPath("users[].id") users 배열 내부의 id 필드
// null 허용 .optional() 필수 아님 (nullable 허용)
// 설명 제외 .ignored() 문서화에 포함하지 않음
// 제약조건 표시 .attributes(key("constraints").value("10자 이상")) 추가 설명이나 제약조건 등 표시