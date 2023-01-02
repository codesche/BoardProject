package project.board.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Member {

    @Id
    private String userId;
    private String userPassword;
    private String userName;
    private String phone;
    private String userGender;
    private String userEmail;

    // 등록일 추가 (2023-01-03)
    private LocalDateTime regDt;

}
