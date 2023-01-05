package project.board.member.service;

import project.board.model.MemberInput;

public interface MemberService {

    /**
     * 회원 가입
     * @param parameter
     * @return
     */
    boolean register(MemberInput parameter);

}
