package project.board.model;

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

}
