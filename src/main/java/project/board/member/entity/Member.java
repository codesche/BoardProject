package project.board.member.entity;

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
public class Member implements MemberCode {

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

    // 패스워드 초기화 칼럼 추가 (2023-01-09)
    private String resetPasswordKey;                // 패스워드 초기화
    private LocalDateTime resetPasswordLimitDt;     // 패스워드 초기화 가능 날짜

    // 관리자 승인 여부 칼럼 추가 (2023-01-18)
    private boolean adminYn;

    // 회원 상태 칼럼 추가 (2023-01-18)
    private String userStatus;                      // 이용가능한 상태, 정지상태, 탈퇴상태

    // 회원 정보 수정일 추가 (2023-01-19)
    private LocalDateTime udtDt;

    // 우편번호, 주소, 상세 주소 칼럼 추가 (2023-01-19)
    private String zipcode;
    private String addr;
    private String addrDetail;


}
