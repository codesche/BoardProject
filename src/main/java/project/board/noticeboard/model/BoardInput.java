package project.board.noticeboard.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoardInput {

    private Long postNumber;             // 게시글 번호  (int형 -> Long으로 변경 2023-01-13)
    private String title;
    private String userId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private String content;

}
