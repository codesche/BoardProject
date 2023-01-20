package project.board.admin.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.board.member.entity.Member;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDTO {

    private String userId;
    private String userPassword;
    private String userName;
    private String phone;
    private String userGender;
    private String userEmail;

    private LocalDateTime regDt;
    private LocalDateTime udtDt;

    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthDt;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYn;
    private String userStatus;

    long totalCount;
    long seq;

    private String zipcode;
    private String addr;
    private String addrDetail;

    LocalDateTime lastLoginDt;

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
            .userId(member.getUserId())
            .userName(member.getUserName())
            .phone(member.getPhone())
            .userGender(member.getUserGender())
            .userEmail(member.getUserEmail())
            .regDt(member.getRegDt())
            .emailAuthYn(member.isEmailAuthYn())
            .emailAuthKey(member.getEmailAuthKey())
            .emailAuthDt(member.getEmailAuthDt())
            .resetPasswordKey(member.getResetPasswordKey())
            .resetPasswordLimitDt(member.getResetPasswordLimitDt())
            .adminYn(member.isAdminYn())
            .userStatus(member.getUserStatus())
            .udtDt(member.getUdtDt())

            .zipcode(member.getZipcode())
            .addr(member.getAddr())
            .addrDetail(member.getAddrDetail())

            .lastLoginDt(member.getLastLoginDt())

            .build();
    }


    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return regDt != null ? this.regDt.format(formatter) : "";
    }

    public String getUdtDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return udtDt != null ? this.udtDt.format(formatter) : "";
    }

    public String getLastLoginDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return lastLoginDt != null ? this.lastLoginDt.format(formatter) : "";
    }


}
