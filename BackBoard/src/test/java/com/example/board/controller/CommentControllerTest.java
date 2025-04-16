package com.example.board.controller;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.comment.dto.*;
import com.example.board.domain.comment.service.CommentService;
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
@WithMockUser
@Import(GlobalExceptionHandler.class)
class CommentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CommentService commentService;

        @Autowired
        private ObjectMapper objectMapper;

        private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWQiOjExLCJuaWNrbmFtZSI6InRlc3QiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTc0NDcwMzY2MSwiZXhwIjoxNzQ0NzkwMDYxfQ.Pfpaw0_AFkV_4_peWl0CY1qegJBs_x-cqlIQLt_pFDs";

        @Test
        void createCommentTest() throws Exception {
                CommentCreateRequestDto request = new CommentCreateRequestDto("TestComment", 1L);
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

                mockMvc.perform(post("/api/comments/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("create-comment",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                requestFields(
                                                                fieldWithPath("postId").description("댓글이 달릴 게시글의 ID"),
                                                                fieldWithPath("content").description("댓글 내용")
                                                                                .attributes(key("constraints")
                                                                                                .value("1~500자"))),
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data").description("응답 데이터 (null)"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void getCommentsByPostIdTest() throws Exception {
                CommentResponseDto comment = CommentResponseDto.builder()
                                .id(1L)
                                .content("Test Comment")
                                .nickname("testUser")
                                .userId(1L)
                                .postId(1L)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                when(commentService.getFindByPostId(anyLong())).thenReturn(List.of(comment));

                mockMvc.perform(get("/api/comments/post/1"))
                                .andExpect(status().isOk())
                                .andDo(document("get-comments",
                                                responseFields(
                                                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                                                fieldWithPath("message").description("응답 메시지"),
                                                                fieldWithPath("data[].id").description("댓글 ID"),
                                                                fieldWithPath("data[].content").description("댓글 내용"),
                                                                fieldWithPath("data[].nickname").description("작성자 닉네임"),
                                                                fieldWithPath("data[].userId").description("작성자 ID"),
                                                                fieldWithPath("data[].postId").description("게시글 ID"),
                                                                fieldWithPath("data[].createdAt").description("생성 시간"),
                                                                fieldWithPath("data[].updatedAt").description("수정 시간"),
                                                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                                                fieldWithPath("errorPath")
                                                                                .description("에러 페이지 경로 (nullable)"))));
        }

        @Test
        void updateCommentTest() throws Exception {
                CommentUpdateRequestDto request = new CommentUpdateRequestDto("Updated Comment");
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

                mockMvc.perform(put("/api/comments/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andDo(document("update-comment",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                requestFields(
                                                                fieldWithPath("content").description("수정할 댓글 내용")
                                                                                .attributes(key("constraints")
                                                                                                .value("1~500자"))),
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
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

                mockMvc.perform(delete("/api/comments/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andDo(document("delete-comment",
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
        void updateCommentFailTest_dueToUnauthorized() throws Exception {
                CommentUpdateRequestDto request = new CommentUpdateRequestDto("test content");
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

                doThrow(new UnauthorizedException("댓글 수정 권한이 없습니다."))
                                .when(commentService).update(anyLong(), any(CommentUpdateRequestDto.class), anyLong());

                mockMvc.perform(put("/api/comments/1")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer "
                                                + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isUnauthorized())
                                .andDo(document("update-comment-fail-unauthorized",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                requestFields(

                                                                fieldWithPath("content").description("수정할 댓글 내용")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (false)"),
                                                                fieldWithPath("message").description("댓글 수정 권한이 없습니다."),
                                                                fieldWithPath("data").description("401 - 인증 실패"),
                                                                fieldWithPath("error").description("UNAUTHORIZED"),
                                                                fieldWithPath("errorPath").description("/error/401"))));
        }

        @Test
        void deleteCommentFailTest_dueToNotFound() throws Exception {
                JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

                doThrow(new BusinessException("존재하지 않는 댓글입니다.", "NOT_FOUND", 404))
                                .when(commentService).delete(anyLong(), anyLong());

                mockMvc.perform(delete("/api/comments/999")
                                .requestAttr("userInfo", mockUserInfo)
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isNotFound())
                                .andDo(document("delete-comment-fail-not-found",
                                                requestHeaders(
                                                                headerWithName("Authorization").description("JWT 토큰")),
                                                responseFields(
                                                                fieldWithPath("success")
                                                                                .description("요청 성공 여부 (false)"),
                                                                fieldWithPath("message").description("존재하지 않는 댓글입니다."),
                                                                fieldWithPath("data").description("404 - 리소스 없음"),
                                                                fieldWithPath("error").description("NOT_FOUND"),
                                                                fieldWithPath("errorPath").description("/error/404"))));
        }
}