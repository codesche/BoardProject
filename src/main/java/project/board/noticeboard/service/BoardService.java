package project.board.noticeboard.service;

import java.util.List;
import project.board.noticeboard.dto.BoardDTO;
import project.board.noticeboard.model.BoardInput;
import project.board.noticeboard.model.BoardParam;

public interface BoardService {

    // 게시판 목록 구현
    List<BoardDTO> list(BoardParam parameter);

    // 게시판 목록에 글 추가
    boolean add(BoardInput parameter);

    // 게시판 글 수정
    boolean set(BoardInput parameter);


    BoardDTO getByPostNumber(long postNumber);

    boolean del(long postNumber);
}
