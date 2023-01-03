package project.board.member.controller;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.entity.Member;
import project.board.member.service.MemberService;
import project.board.model.MemberInput;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

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

    // request      WEB -> SERVER
    // response     SERVER -> WEB
    @PostMapping("/member/register")
    public String registerSubmit(Model model, HttpServletRequest request
        , HttpServletResponse response
        , MemberInput parameter) {

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

}
