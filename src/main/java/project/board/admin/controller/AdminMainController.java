package project.board.admin.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMainController {

    @GetMapping("/admin/main")
    @ApiOperation(value = "관리자 메인 페이지 화면을 보여줍니다")
    public String main() {
        return "admin/main";
    }

}
