package project.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    // 에러 메시지 처리
    @RequestMapping("/error/denied")
    public String errorDenied() {

        return "error/denied";
    }


}
