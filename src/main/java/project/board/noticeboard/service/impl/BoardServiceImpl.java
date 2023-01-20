package project.board.noticeboard.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import project.board.BoardApplication;
import project.board.noticeboard.dto.BoardDTO;
import project.board.noticeboard.entity.Board;
import project.board.noticeboard.mapper.BoardMapper;
import project.board.noticeboard.model.BoardInput;
import project.board.noticeboard.model.BoardParam;
import project.board.noticeboard.repository.BoardRepository;
import project.board.noticeboard.service.BoardService;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final BoardMapper boardMapper;

    private static final Logger logger = LoggerFactory.getLogger(BoardApplication.class);

    @Override
    public List<BoardDTO> list(BoardParam parameter) {

        logger.info("=== 게시판 글 조회(Start) ===");

        long totalCount = boardMapper.selectListCount(parameter);
        List<BoardDTO> list = boardMapper.selectList(parameter);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (BoardDTO x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        logger.info("=== 게시판 글 조회(End) ===");

        return list;
    }

    // 게시글 추가
    @Override
    public boolean add(BoardInput parameter) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        LocalDateTime now = LocalDateTime.now();

        logger.info("=== 게시판 글 추가(Start) ===");

        Board board = Board.builder()
            .postNumber(parameter.getPostNumber())
            .title(parameter.getTitle())
            .updateDt(LocalDateTime.of(now.getYear(), now.getMonth(),
                now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()))
            .createDt(LocalDateTime.of(now.getYear(), now.getMonth(),
                now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()))
            .content(parameter.getContent())
            .userId(userDetails.getUsername())
            .build();

        boardRepository.save(board);

        logger.info("=== 게시판 글 추가(End) ===");

        return true;
    }

    // 게시글 수정
    public boolean set(BoardInput parameter) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        LocalDateTime now = LocalDateTime.now();

        logger.info("=== 게시판 글 수정(Start) ===");

        Optional<Board> optionalBoard = boardRepository.findById(parameter.getPostNumber());
        if (optionalBoard.isEmpty()) {
            // 수정할 데이터 없음
            return false;
        }

        Board board = optionalBoard.get();
        board.setPostNumber(parameter.getPostNumber());
        board.setTitle(parameter.getTitle());
        board.setUserId(userDetails.getUsername());
        board.setUpdateDt(LocalDateTime.of(now.getYear(), now.getMonth(),
            now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()));
        board.setContent(parameter.getContent());

        boardRepository.save(board);

        logger.info("=== 게시판 글 수정(End) ===");

        return true;
    }


    @Override
    public BoardDTO getByPostNumber(long postNumber) {
        return boardRepository.findById(postNumber).map(BoardDTO::of).orElse(null);
    }

    @Override
    public boolean del(long postNumber) {

        boardRepository.deleteById(postNumber);

        return true;
    }

    @Override
    public BoardDTO detail(Long postNumber) {

        logger.info("=== 게시판 글 상세 조회(Start) ===");

        Optional<Board> optionalBoard = boardRepository.findById(postNumber);
        if (optionalBoard.isEmpty()) {
            return null;
        }

        Board board = optionalBoard.get();

        logger.info("=== 게시판 글 상세 조회(End) ===");

        return BoardDTO.of(board);
    }

    @Override
    public void countVisit(Long postNumber) {
        boardMapper.countVisit(postNumber);
    }


}
