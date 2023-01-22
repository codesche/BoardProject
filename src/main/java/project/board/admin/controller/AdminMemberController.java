package project.board.admin.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.board.admin.dto.LoginHistoryDTO;
import project.board.admin.dto.MemberDTO;
import project.board.admin.model.MemberStatusInput;
import project.board.admin.model.MemberParam;
import project.board.common.BaseController;
import project.board.member.service.MemberService;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {

    private final MemberService memberservice;

    @GetMapping("/admin/member/list")
    @ApiOperation(value = "관리자 - 회원 관리 목록 호출")
    public String list(Model model, MemberParam parameter) {

        parameter.init();
        List<MemberDTO> members = memberservice.list(parameter);

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(),
            parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";

    }

    @GetMapping("/admin/member/detail")
    @ApiOperation(value = "관리자 - 회원 관리 상세 목록 호출")
    public String detail(Model model, MemberParam parameter) {

        parameter.init();

        MemberDTO member = memberservice.detail(parameter.getUserId());
        model.addAttribute("member", member);

        List<LoginHistoryDTO> histories = memberservice.loginHistory(parameter.getUserId());
        model.addAttribute("histories", histories);

        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status")
    @ApiOperation(value = "관리자 - 회원 상태 변경")
    public String status(Model model, MemberStatusInput parameter) {
        boolean result = memberservice.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        return "redirect:/admin/member/detail?userId=" + parameter.getUserId();
    }

    @PostMapping("/admin/member/password")
    @ApiOperation(value = "관리자 - 비밀번호 변경")
    public String password(MemberStatusInput parameter) {
        boolean result = memberservice.updatePassword(parameter.getUserId(), parameter.getPassword());

        return "redirect:/admin/member/detail?userId=" + parameter.getUserId();
    }



}
