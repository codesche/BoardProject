package project.board.noticeboard.service;

import java.util.List;
import project.board.noticeboard.dto.BoardDTO;
import project.board.noticeboard.model.BoardInput;
import project.board.noticeboard.model.BoardParam;

public interface BoardService {

    /**
     * 게시판 글 목록
     */
    List<BoardDTO> list(BoardParam parameter);

    /**
     * 게시판 글 추가
     */
    boolean add(BoardInput parameter);

    /**
     * 게시판 글 수정
     */
    boolean set(BoardInput parameter);

    BoardDTO getByPostNumber(long postNumber);

    /**
     * 게시판 글 삭제
     */
    boolean del(long postNumber);

    BoardDTO detail(Long postNumber);

    void countVisit(Long postNumber);
}
