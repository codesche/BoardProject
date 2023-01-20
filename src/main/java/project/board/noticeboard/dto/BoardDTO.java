package project.board.noticeboard.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private Long countVisit;

    // 추가컬럼
    long totalCount;
    long seq;

    private String loginId;

    public String getLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        loginId = userDetails.getUsername();

        return loginId;
    }


    public static BoardDTO of(Board board) {

        return BoardDTO.builder()
                        .postNumber(board.getPostNumber())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .userId(board.getUserId())
                        .createDt(board.getCreateDt())
                        .updateDt(board.getUpdateDt())
                        .countVisit(board.getCountVisit())
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

    public String getUpdateDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return updateDt != null ? this.updateDt.format(formatter) : "";
    }

    public String getCreateDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createDt != null ? this.createDt.format(formatter) : "";
    }


}
