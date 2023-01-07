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

    // 이메일 인증 관련 칼럼 추가 (2023-01-06)
    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthDt;
//    private String userStatus;                      // 계정 이용가능한 상태, 정지상태 여부 구분


}
