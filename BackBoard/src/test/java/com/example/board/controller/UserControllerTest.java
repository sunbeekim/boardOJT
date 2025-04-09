package com.example.board.controller;

import com.example.board.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.example.board.domain.user.dto.SignUpRequestDto;
import com.example.board.service.*;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtUtil jwtUtil;

        @Test
        void signUpTest() throws Exception {
                SignUpRequestDto request = new SignUpRequestDto("test@email.com", "password123!", "nickname");

                mockMvc.perform(post("/api/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("signup",
                                                requestFields(
                                                                fieldWithPath("email").description("회원 이메일"),
                                                                fieldWithPath("password").description("비밀번호")
                                                                                .attributes(key("constraints").value(
                                                                                                "8~16자, 영문, 숫자, 특수문자 필수 포함")),
                                                                fieldWithPath("nickname").description("닉네임")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (true/false)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (200)"),
                                                                fieldWithPath("error")
                                                                                .description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void signUpFailTest_dueToPasswordMissing() throws Exception {
                SignUpRequestDto request = new SignUpRequestDto("test@email.com", "", "nickname");

                mockMvc.perform(post("/api/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andDo(document("signup-fail", responseFields(
                                                fieldWithPath("success")
                                                                .description("요청 성공 여부 (false)"),
                                                fieldWithPath("message").description("Bad Request"),
                                                fieldWithPath("data").description(
                                                                "400 - DTO 조건 불충족족(요청 바디 검증 실패)"),
                                                fieldWithPath("error").description("CONSTRAINT_VIOLATION"),
                                                fieldWithPath("errorPath").description("/error/400")

                                )

                                ));

        }
}

// 타입 예시 설명
// 단일 필드 fieldWithPath("email") 단순한 키 필드
// 중첩 객체 fieldWithPath("user.email") user 안의 email 필드
// 리스트 fieldWithPath("users[]") users라는 배열
// 리스트 내 객체 fieldWithPath("users[].id") users 배열 내부의 id 필드
// null 허용 .optional() 필수 아님 (nullable 허용)
// 설명 제외 .ignored() 문서화에 포함하지 않음
// 제약조건 표시 .attributes(key("constraints").value("10자 이상")) 추가 설명이나 제약조건 등 표시