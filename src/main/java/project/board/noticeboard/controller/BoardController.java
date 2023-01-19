package project.board.noticeboard.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.board.common.BaseController;
import project.board.noticeboard.dto.BoardDTO;
import project.board.noticeboard.entity.Board;
import project.board.noticeboard.model.BoardInput;
import project.board.noticeboard.model.BoardParam;
import project.board.noticeboard.service.BoardService;

@RequiredArgsConstructor
@Controller
public class BoardController extends BaseController {

    private final BoardService boardService;

    // 게시판 목록 조회
    @GetMapping("/board/list")
    public String list(Model model, BoardParam parameter) {

        parameter.init();
        List<BoardDTO> boards = boardService.list(parameter);

        long totalCount = 0;

        if (!CollectionUtils.isEmpty(boards)) {
            totalCount = boards.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(),
                                        parameter.getPageIndex(), queryString);

        model.addAttribute("list", boards);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "board/list";
    }

    @GetMapping(value = {"/board/write", "/board/edit"})
    public String write(Model model, HttpServletRequest request, BoardInput parameter) {

        boolean editMode = request.getRequestURI().contains("/edit");
        BoardDTO detail = new BoardDTO();

        if (editMode) {
            long postNumber = parameter.getPostNumber();
            BoardDTO existBoard = boardService.getByPostNumber(postNumber);
            if (existBoard == null) {
                // error 처리
                model.addAttribute("message", "해당 게시글이 존재하지 않습니다.");
                return "error/denied";
            }
            detail = existBoard;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "board/write";
    }

    @PostMapping(value = {"/board/write", "/board/edit"})
    public String writeSubmit(Model model, HttpServletRequest request, BoardInput parameter) {

        boolean editMode = request.getRequestURI().contains("/edit");

        if (editMode) {
            long postNumber = parameter.getPostNumber();
            BoardDTO existBoard = boardService.getByPostNumber(postNumber);
            if (existBoard == null) {
                // error 처리
                model.addAttribute("message", "해당 게시글이 존재하지 않습니다.");
                return "error/denied";
            }

            boolean result = boardService.set(parameter);
        } else {
            boolean result = boardService.add(parameter);
        }

        return "redirect:/board/list";

    }

    // 게시글 삭제
    @PostMapping("/board/write/delete")
    public String del(Model model, BoardInput parameter) {
        boolean result = boardService.del(parameter.getPostNumber());

        return "redirect:/board/list";
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/board/detail")
    public String detail(Model model, BoardParam parameter) {

        parameter.init();
        boardService.countVisit(parameter.getPostNumber());

        BoardDTO board = boardService.detail(parameter.getPostNumber());
        model.addAttribute("board", board);

        return "board/detail";

    }

}
