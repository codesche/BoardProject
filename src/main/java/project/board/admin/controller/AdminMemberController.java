package project.board.admin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.board.admin.dto.MemberDTO;
import project.board.admin.model.MemberParam;
import project.board.common.BaseController;
import project.board.member.service.MemberService;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {

    private final MemberService memberservice;

    @GetMapping("/admin/member/list")
    public String list(Model model, MemberParam parameter) {

        parameter.init();
        List<MemberDTO> members = memberservice.list(parameter);

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";

    }

    @GetMapping("/admin/member/detail")
    public String detail(Model model, MemberParam parameter) {

        parameter.init();

        MemberDTO member = memberservice.detail(parameter.getUserId());
        model.addAttribute("member", member);

        return "admin/member/detail";
    }

}
