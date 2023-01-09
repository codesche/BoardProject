package project.board.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResetPasswordInput {

    private String userEmail;

    private String id;              // uuid
    private String password;

}
