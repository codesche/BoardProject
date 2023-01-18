package project.board.member.entity;

public interface MemberCode {

    /**
     * 현재 가입 요청중
     */
    String MEMBER_STATUS_REQ = "REQ";

    /**
     * 현재 이용중인 상태
     */
    String MEMBER_STATUS_ING = "ING";

    /**
     * 현재 정지된 상태
     */
    String MEMBER_STATUS_STOP = "STOP";

    /**
     * 현재 탈퇴된 회원
     */
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";

}
