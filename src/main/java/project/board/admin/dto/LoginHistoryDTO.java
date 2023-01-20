package project.board.admin.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.board.member.entity.LoginHistory;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginHistoryDTO {

    private Long seq;
    private String userId;
    private LocalDateTime loginDt;
    private String clientIp;
    private String userAgent;

    public static LoginHistoryDTO of(LoginHistory loginHistory) {
        return LoginHistoryDTO.builder()
                .userId(loginHistory.getUserId())
                .loginDt(loginHistory.getLoginDt())
                .clientIp(loginHistory.getClientIp())
                .userAgent(loginHistory.getUserAgent())
                .build();
    }

    public String getLastLoginDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return loginDt != null ? this.loginDt.format(formatter) : "";
    }


}
