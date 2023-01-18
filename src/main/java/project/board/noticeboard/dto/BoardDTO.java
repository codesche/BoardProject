package project.board.noticeboard.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import project.board.noticeboard.entity.Board;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long postNumber;             // 게시글 번호
    private String title;
    private String userId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private String content;

    // 추가컬럼
    long totalCount;
    long seq;

    public static BoardDTO of(Board board) {

        return BoardDTO.builder()
                        .postNumber(board.getPostNumber())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .userId(board.getUserId())
                        .createDt(board.getCreateDt())
                        .updateDt(board.getUpdateDt())
                        .build();
    }

    public static List<BoardDTO> of(List<Board> boards) {

        if (boards == null) {
            return null;
        }

        List<BoardDTO> boardList = new ArrayList<>();
        for (Board x : boards) {
            boardList.add(BoardDTO.of(x));
        }

        return boardList;

    }



}
