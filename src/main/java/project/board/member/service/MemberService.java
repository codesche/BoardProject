package project.board.member.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.board.model.MemberInput;

public interface MemberService extends UserDetailsService {

    /**
     * 회원 가입
     */
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 활성화 함
     */
    boolean emailAuth(String uuid);
}
