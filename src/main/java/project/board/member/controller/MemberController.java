package project.board.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.member.service.MemberService;
import project.board.model.MemberInput;
import project.board.model.ResetPasswordInput;

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

    // 메일 인증
    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email_auth";
    }

    // 회원 정보
    @GetMapping("/member/info")
    public String memberInfo() {

        return "member/info";

    }

    // 비밀번호 찾기
    @GetMapping("/member/find/password")
    public String findPassword() {
        return "member/find_password";
    }

    // 비밀번호 찾기 요청
    @PostMapping("/member/find/password")
    public String findPasswordSubmit(Model model, ResetPasswordInput parameter) {

        boolean result = false;

        try {
            result = memberService.sendResetPassword(parameter);
        } catch (Exception e) {

        }
        model.addAttribute("result", result);

        return "member/find_password_result";

    }


    // 비밀번호 초기화
    @GetMapping("/member/reset/password")
    public String resetPassword(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id");

        boolean result = memberService.checkResetPassword(uuid);

        model.addAttribute("result", result);

        return "member/reset_password";
    }


    // 비밀번호 초기화 요청
    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput parameter) {

        boolean result = false;

        try {
            result = memberService.resetPassword(parameter.getId(), parameter.getPassword());
        } catch (Exception e) {

        }

        model.addAttribute("result", result);

        return "member/reset_password_result";
    }



}
