package project.board.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    @ApiOperation(value = "메인 화면")
    public String index() {
        return "index";
    }

    // 에러 메시지 처리
    @ApiOperation(value = "에러 발생 화면")
    @RequestMapping("/error/denied")
    public String errorDenied() {

        return "error/denied";
    }


}
