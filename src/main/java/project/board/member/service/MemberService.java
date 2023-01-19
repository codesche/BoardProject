package project.board.member.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import project.board.admin.dto.MemberDTO;
import project.board.admin.model.MemberParam;
import project.board.member.model.MemberInput;
import project.board.member.model.ResetPasswordInput;
import project.board.member.model.ServiceResult;

public interface MemberService extends UserDetailsService {

    /**
     * 회원 가입
     */
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 활성화 함
     */
    boolean emailAuth(String uuid);

    /**
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     */
    boolean sendResetPassword(ResetPasswordInput parameter);

    /**
     * 입력받은 uuid에 대해서 password로 초기화 함
     */
    boolean resetPassword(String id, String password);

    /**
     * 입력받은 uuid값이 유효한지 확인
     */
    boolean checkResetPassword(String uuid);

    /**
     * 회원 목록 리턴 (관리자에서만 사용 가능)
     */
    List<MemberDTO> list(MemberParam parameter);

    /**
     * 회원 상세 정보
     */
    MemberDTO detail(String userId);

    /**
     * 회원 상태 변경
     */
    boolean updateStatus(String userId, String userStatus);

    /**
     * 회원 비밀번호 초기화
     */
    boolean updatePassword(String userId, String password);

    /**
     * 회원정보 수정
     */
    ServiceResult updateMember(MemberInput parameter);

    /**
     * 회원 정보 페이지내 비밀번호 변경 가능
     */
    ServiceResult updateMemberPassword(MemberInput parameter);

    /**
     * 회원 탈퇴
     */
    ServiceResult withdraw(String userId, String userPassword);

}
