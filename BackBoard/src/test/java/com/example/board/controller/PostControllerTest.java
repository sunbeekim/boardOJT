package com.example.board.controller;

import com.example.board.common.dto.JwtUserInfo;
import com.example.board.domain.post.dto.*;
import com.example.board.domain.post.service.PostService;
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
@WithMockUser
@Import(GlobalExceptionHandler.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NUB0ZXN0LmNvbSIsImlkIjo4LCJuaWNrbmFtZSI6Iu2FjOyKpO2KuOycoOyggDUiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQ0MjYxNjY1LCJleHAiOjE3NDQzNDgwNjV9.WpsAqbEShHPybqDd0p-YVUkLQoMY379Kr3Vu6_u_5_I";

    @Test
    void createPostTest() throws Exception {
        PostCreateRequestDto request = new PostCreateRequestDto("Test Title", "Test Content");
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

        mockMvc.perform(post("/api/posts")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("create-post",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목")
                                        .attributes(key("constraints").value("1~100자")),
                                fieldWithPath("content").description("게시글 내용")
                                        .attributes(key("constraints").value("1~1000자"))),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터 (null)"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void getPostTest() throws Exception {
        PostResponseDto response = PostResponseDto.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .nickname("testUser")
                .userId(1L)
                .viewCount(0)
                .commentCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.getPostById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andDo(document("get-post",
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("게시글 ID"),
                                fieldWithPath("data.title").description("게시글 제목"),
                                fieldWithPath("data.content").description("게시글 내용"),
                                fieldWithPath("data.nickname").description("작성자 닉네임"),
                                fieldWithPath("data.userId").description("작성자 ID"),
                                fieldWithPath("data.viewCount").description("조회수"),
                                fieldWithPath("data.commentCount").description("댓글 수"),
                                fieldWithPath("data.createdAt").description("생성 시간"),
                                fieldWithPath("data.updatedAt").description("수정 시간"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void updatePostTest() throws Exception {
        PostUpdateRequestDto request = new PostUpdateRequestDto("Updated Title", "Updated Content");
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

        mockMvc.perform(put("/api/posts/1")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("update-post",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("title").description("수정할 게시글 제목")
                                        .attributes(key("constraints").value("1~100자")),
                                fieldWithPath("content").description("수정할 게시글 내용")
                                        .attributes(key("constraints").value("1~1000자"))),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터 (null)"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void deletePostTest() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

        mockMvc.perform(delete("/api/posts/1")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(document("delete-post",
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
    void searchPostsTest() throws Exception {
        PostResponseDto post = PostResponseDto.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .nickname("testUser")
                .userId(1L)
                .viewCount(0)
                .commentCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.searchPosts(any(PostSearchConditionDto.class))).thenReturn(List.of(post));

        mockMvc.perform(get("/api/posts")
                .param("title", "Test")
                .param("nickname", "testUser")
                .param("dateFrom", "2024-01-01T00:00:00")
                .param("dateTo", "2024-12-31T23:59:59")
                .param("sort", "createdAt")
                .param("direction", "DESC")
                .param("size", "10")
                .param("offset", "0"))
                .andExpect(status().isOk())
                .andDo(document("search-posts",
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (true)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("게시글 ID"),
                                fieldWithPath("data[].title").description("게시글 제목"),
                                fieldWithPath("data[].content").description("게시글 내용"),
                                fieldWithPath("data[].nickname").description("작성자 닉네임"),
                                fieldWithPath("data[].userId").description("작성자 ID"),
                                fieldWithPath("data[].viewCount").description("조회수"),
                                fieldWithPath("data[].commentCount").description("댓글 수"),
                                fieldWithPath("data[].createdAt").description("생성 시간"),
                                fieldWithPath("data[].updatedAt").description("수정 시간"),
                                fieldWithPath("error").description("에러 정보 (nullable)"),
                                fieldWithPath("errorPath").description("에러 페이지 경로 (nullable)"))));
    }

    @Test
    void updatePostFailTest_dueToUnauthorized() throws Exception {
        PostUpdateRequestDto request = new PostUpdateRequestDto("Updated Title", "Updated Content");
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

        when(postService.updatePost(anyLong(), any(PostUpdateRequestDto.class), anyLong()))
                .thenThrow(new UnauthorizedException("게시글 수정 권한이 없습니다."));

        mockMvc.perform(put("/api/posts/1")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(document("update-post-fail-unauthorized",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        requestFields(
                                fieldWithPath("title").description("수정할 게시글 제목"),
                                fieldWithPath("content").description("수정할 게시글 내용")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (false)"),
                                fieldWithPath("message").description("게시글 수정 권한이 없습니다."),
                                fieldWithPath("data").description("401 - 인증 실패"),
                                fieldWithPath("error").description("UNAUTHORIZED"),
                                fieldWithPath("errorPath").description("/error/401"))));
    }

    @Test
    void deletePostFailTest_dueToNotFound() throws Exception {
        JwtUserInfo mockUserInfo = new JwtUserInfo(1L, "test@email.com", "nickname");

        when(postService.deletePost(anyLong(), anyLong()))
                .thenThrow(new BusinessException("존재하지 않는 게시글입니다.", "NOT_FOUND", 404));

        mockMvc.perform(delete("/api/posts/999")
                .requestAttr("userInfo", mockUserInfo)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(document("delete-post-fail-not-found",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부 (false)"),
                                fieldWithPath("message").description("존재하지 않는 게시글입니다."),
                                fieldWithPath("data").description("404 - 리소스 없음"),
                                fieldWithPath("error").description("NOT_FOUND"),
                                fieldWithPath("errorPath").description("/error/404"))));
    }
} 