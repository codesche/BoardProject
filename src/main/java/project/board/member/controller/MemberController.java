package project.board.member.controller;

import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.admin.dto.MemberDTO;
import project.board.admin.model.MemberParam;
import project.board.common.BaseController;
import project.board.member.model.ServiceResult;
import project.board.member.service.MemberService;
import project.board.member.model.MemberInput;
import project.board.member.model.ResetPasswordInput;

@RequiredArgsConstructor
@Controller
public class MemberController extends BaseController {

    private final MemberService memberService;

    // 로그인
    @RequestMapping("/member/login")
    @ApiOperation("로그인")
    public String login() {

        return "member/login";
    }

    // 회원가입
    @GetMapping("/member/register")
    @ApiOperation(value = "회원가입")
    public String register() {
        return "member/register";
    }

    // request      WEB -> SERVER
    // response     SERVER -> WEB
    @PostMapping("/member/register")
    @ApiOperation(value = "회원가입")
    public String registerSubmit(Model model, HttpServletRequest request
        , HttpServletResponse response
        , MemberInput parameter) {

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    // 메일 인증
    @GetMapping("/member/email-auth")
    @ApiOperation(value = "회원 가입 후 메일 인증")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email_auth";
    }

    // 회원 정보
    @GetMapping("/member/info")
    @ApiOperation(value = "사용자 - 회원 상세 정보 확인")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDTO detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/info";

    }

    // 회원 정보 수정
    @PostMapping("/member/info")
    @ApiOperation(value = "사용자 - 회원 상세 정보 확인")
    public String memberInfoSubmit(Model model, MemberInput parameter, Principal principal) {

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMember(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "error/denied";
        }

        return "redirect:/member/info";
    }


    // 비밀번호 찾기
    @GetMapping("/member/find/password")
    @ApiOperation(value = "비밀번호 찾기")
    public String findPassword() {
        return "member/find_password";
    }

    // 비밀번호 찾기 요청
    @PostMapping("/member/find/password")
    @ApiOperation(value = "비밀번호 찾기")
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
    @ApiOperation(value = "비밀번호 초기화")
    public String resetPassword(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id");

        boolean result = memberService.checkResetPassword(uuid);

        model.addAttribute("result", result);

        return "member/reset_password";
    }


    // 비밀번호 초기화 요청
    @PostMapping("/member/reset/password")
    @ApiOperation(value = "비밀번호 초기화")
    public String resetPasswordSubmit(Model model, ResetPasswordInput parameter) {

        boolean result = false;

        try {
            result = memberService.resetPassword(parameter.getId(), parameter.getPassword());
        } catch (Exception e) {

        }

        model.addAttribute("result", result);

        return "member/reset_password_result";
    }

    // 회원 정보 메뉴에서 비밀번호 변경
    @GetMapping("/member/password")
    @ApiOperation(value = "비밀번호 초기화")
    public String memberPassword(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDTO detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/password";
    }

    @PostMapping("/member/password")
    @ApiOperation(value = "비밀번호 초기화")
    public String memberPasswordSubmit(Model model, MemberInput parameter, Principal principal) {

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMemberPassword(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "error/denied";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/member/withdraw")
    @ApiOperation(value = "회원 탈퇴")
    public String memberWithDraw(Model model) {

        return "member/withdraw";

    }

    @PostMapping("/member/withdraw")
    @ApiOperation(value = "회원 탈퇴")
    public String memberWithdrawSubmit(Model model, MemberInput parameter, Principal principal) {

        String userId = principal.getName();

        ServiceResult result = memberService.withdraw(userId, parameter.getUserPassword());
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "error/denied";
        }

        return "redirect:/member/logout";
    }


    @GetMapping("/member/list")
    @ApiOperation(value = "회원 목록 조회")
    public String list(Model model, MemberParam memberParam) {

        memberParam.init();
        List<MemberDTO> members = memberService.list(memberParam);

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }

        String queryString = memberParam.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, memberParam.getPageSize(),
            memberParam.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "member/list";

    }


}
