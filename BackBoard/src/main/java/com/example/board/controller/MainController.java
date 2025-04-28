package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

// 메인 페이지 컨트롤러
// html(view) Thymeleaf 템플릿 파일들을 렌더링하는 역할
@Controller
public class MainController {

    // 메인 페이지
    @GetMapping("/") // 브라우저 url 주소
    public String home() {
        return "login"; // path : templates/home.html 파일을 route
    }

    // 게시판 페이지
    @GetMapping("/board/admin/admin_user_list")
    public String admin_user_list() {
        return "board/admin_user_list";
    }

    // 게시판 페이지
    @GetMapping("/board/admin/admin_article_list")
    public String admin_article_list() {
        return "board/admin/admin_article_list";
    }

    // 게시판 페이지
    @GetMapping("/board/admin/admin_post_01")
    public String admin_post_01() {
        return "/board/admin/admin_post_01";
    }

    // 게시판 목록 페이지
    @GetMapping("/board/board")
    public String boardList() {
        return "board/board";
    }

    // 게시글 등록
    @GetMapping("/board/post_01")
    public String boardRead(@RequestParam(required = false) Long id) { // mvc 어노테이션 RequestParam 사용해서 쿼리 파라미터 값 받기
        if (id == null) {
            return "redirect:/board/board";
        }
        return "board/post_01";
    }

    // 게시판 수정 페이지
    @GetMapping("/board/edit_post_01")
    public String boardEdit(@RequestParam(required = false) Long id) {
        if (id == null) {
            return "board/edit_post_01";
        }
        return "board/edit_post_01";
    }

    // 유저 정보 페이지
    @GetMapping("/user_info")
    public String movieSearch() {
        return "user_info";
    }

    // 유저 정보 수정 페이지
    @GetMapping("/user_info_edit")
    public String movieRank() {
        return "user_info_edit";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}