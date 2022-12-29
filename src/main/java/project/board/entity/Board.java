package project.board.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Board {
    @Id
    private String title;
    private String userId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private String content;
    private int isDeleteYn;         // 게시판 글 삭제 여부 확인

}
