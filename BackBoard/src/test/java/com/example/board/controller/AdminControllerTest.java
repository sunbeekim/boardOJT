package com.example.board.controller;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.admin.dto.*;
import com.example.board.domain.admin.service.AdminService;
import com.example.board.exception.BusinessException;
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
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsImlkIjoxLCJuaWNrbmFtZSI6ImFkbWluIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3NDQyNjE2NjUsImV4cCI6MTc0NDM0ODA2NX0.abcdefghijklmnopqrstuvwxyz";

    @Test
    void getAllUsersTest() throws Exception {
        UserResponseDto user = UserResponseDto.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("testUser")
                .role("ROLE_USER")
                .isLocked(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(adminService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(document("get-all-users",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("사용자 ID"),
                                fieldWithPath("data[].email").description("이메일"),
                                fieldWithPath("data[].nickname").description("닉네임"),
                                fieldWithPath("data[].role").description("사용자 역할"),
                                fieldWithPath("data[].isLocked").description("계정 잠금 상태"),
                                fieldWithPath("data[].createdAt").description("생성 시간"),
                                fieldWithPath("data[].updatedAt").description("수정 시간"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void updateUserRoleTest() throws Exception {
        UserRoleUpdateRequestDto request = new UserRoleUpdateRequestDto("ROLE_ADMIN");
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        mockMvc.perform(put("/api/admin/users/1/role")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("update-user-role",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("role").description("변경할 역할")
                                        .attributes(key("constraints").value("ROLE_USER, ROLE_ADMIN"))),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터 (null)"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void lockUserTest() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        mockMvc.perform(put("/api/admin/users/1/lock")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(document("lock-user",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터 (null)"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void unlockUserTest() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        mockMvc.perform(put("/api/admin/users/1/unlock")
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
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void deleteUserTest() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        mockMvc.perform(delete("/api/admin/users/1")
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
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void updateUserRoleFailTest_dueToUnauthorized() throws Exception {
        UserRoleUpdateRequestDto request = new UserRoleUpdateRequestDto("ROLE_ADMIN");
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        when(adminService.updateUserRole(anyLong(), any(UserRoleUpdateRequestDto.class)))
                .thenThrow(new UnauthorizedException("관리자 권한이 필요합니다."));

        mockMvc.perform(put("/api/admin/users/1/role")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(document("update-user-role-fail-unauthorized",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("role").description("변경할 역할")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (false)"),
                                fieldWithPath("message").description("관리자 권한이 필요합니다."),
                                fieldWithPath("data").description("401 - 인증 실패"),
                                fieldWithPath("error").description("UNAUTHORIZED"),
                                fieldWithPath("errorPath").description("/error/401"))));
    }

    @Test
    void deleteUserFailTest_dueToNotFound() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "admin@test.com", "admin");

        when(adminService.deleteUser(anyLong()))
                .thenThrow(new BusinessException("존재하지 않는 사용자입니다.", "NOT_FOUND", 404));

        mockMvc.perform(delete("/api/admin/users/999")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(document("delete-user-fail-not-found",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (false)"),
                                fieldWithPath("message").description("존재하지 않는 사용자입니다."),
                                fieldWithPath("data").description("404 - 리소스 없음"),
                                fieldWithPath("error").description("NOT_FOUND"),
                                fieldWithPath("errorPath").description("/error/404"))));
    }
} 