package com.example.board.controller;

import com.example.board.dto.SignUpRequestDto;
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
                                                                fieldWithPath("password").description("비밀번호"),
                                                                fieldWithPath("nickname").description("닉네임")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (true/false)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (nullable)"),
                                                                fieldWithPath("error")
                                                                                .description("에러 정보 (nullable)"))));
        }
}