package project.board.noticeboard.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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

    @Override
    public List<BoardDTO> list(BoardParam parameter) {

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

        return list;
    }

    // 게시글 추가
    @Override
    public boolean add(BoardInput parameter) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        LocalDateTime now = LocalDateTime.now();

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

        return true;
    }

    // 게시글 수정
    public boolean set(BoardInput parameter) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        LocalDateTime now = LocalDateTime.now();

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

        Optional<Board> optionalBoard = boardRepository.findById(postNumber);
        if (optionalBoard.isEmpty()) {
            return null;
        }

        Board board = optionalBoard.get();

        return BoardDTO.of(board);
    }


}
