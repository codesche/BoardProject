package project.board.noticeboard.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNumber;             // 게시글 번호  (int형 -> Long으로 변경 2023-01-13)
    private String title;
    private String userId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    @Lob
    private String content;
}
