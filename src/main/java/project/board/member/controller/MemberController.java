package project.board.member.controller;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.entity.Member;
import project.board.member.repository.MemberRepository;
import project.board.model.MemberInput;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberRepository memberRepository;

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

    @PostMapping("/member/register")
    public String registerSubmit(HttpServletRequest request
            , HttpServletResponse response
            , MemberInput parameter) {

        System.out.println("################### - 1");
        System.out.println(parameter.toString());

        Member member = new Member();
        member.setUserId(parameter.getUserId());
        member.setUserPassword(parameter.getUserPassword());
        member.setUserName(parameter.getUserName());
        member.setPhone(parameter.getPhone());
        member.setUserGender(parameter.getUserGender());
        member.setUserEmail(parameter.getUserEmail());
        member.setRegDt(LocalDateTime.now());

        memberRepository.save(member);

        return "member/register_complete";
    }

}
