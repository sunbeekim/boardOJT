package com.example.board.controller;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.user.dto.*;
import com.example.board.domain.user.entity.User;
import com.example.board.domain.user.enums.UserRole;
import com.example.board.domain.user.service.AdminUserService;
import com.example.board.exception.BusinessException;
import com.example.board.exception.GlobalExceptionHandler;
import com.example.board.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser(roles = "ADMIN")
@Import(GlobalExceptionHandler.class)
class AdminControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AdminUserService adminService;

        @Autowired
        private ObjectMapper objectMapper;

        private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpZCI6MTAsIm5pY2tuYW1lIjoiQWRtaW4iLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTc0NDcwMzU4MywiZXhwIjoxNzQ0Nzg5OTgzfQ.F7vhdPb2Qft956AtDTg5Vzt5-hpcNnKvyBetvm3EWIo";

        // 관리자 전용 계정 잠금 기능 추가 시 사용 테스트
        // @Test
        // void lockUserTest() throws Exception {
        // JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        // mockMvc.perform(patch("/api/admin/users/1/unlock")
        // .requestAttr("userInfo", mockUserInfo)
        // .header("Authorization", "Bearer " + token))
        // .andExpect(status().isOk())
        // .andDo(document("lock-user",
        // requestHeaders(
        // headerWithName("Authorization").description("JWT 토큰")),
        // responseFields(
        // fieldWithPath("success").description("요청 성공 여부 (true)"),
        // fieldWithPath("message").description("응답 메시지"),
        // fieldWithPath("data").description("응답 데이터 (null)"),
        // fieldWithPath("error").description("에러 정보 (nullable)"),
        // fieldWithPath("errorPath")
        // .description("에러 페이지 경로 (nullable)"))));
        // }

        @Test
        void unlockUserTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

                mockMvc.perform(patch("/api/admin/users/1/unlock")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("unlock-user",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void deleteUserTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

                mockMvc.perform(delete("/api/admin/users/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("admin-delete-user",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void deletePostTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

                mockMvc.perform(delete("/api/admin/posts/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("admin-delete-post",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void deleteCommentTest() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

                mockMvc.perform(delete("/api/admin/comments/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("admin-delete-comment",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

}