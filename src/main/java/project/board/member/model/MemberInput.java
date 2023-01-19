package project.board.member.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberInput {

    private String userId;
    private String userPassword;
    private String userName;
    private String phone;
    private String userGender;
    private String userEmail;

    private String newPassword;

    private String zipcode;
    private String addr;
    private String addrDetail;

}
