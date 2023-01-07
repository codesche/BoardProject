package project.board.member.service;

import project.board.model.MemberInput;

public interface MemberService {

    /**
     * 회원 가입
     */
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 활성화 함
     */
    boolean emailAuth(String uuid);
}
