package project.board.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {

    // 로그인
    @RequestMapping("/member/login")
    public String login() {
        return "member/login";
    }

    // 회원가입
    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }

}
