package project.board.member.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import project.board.BoardApplication;
import project.board.admin.dto.LoginHistoryDTO;
import project.board.admin.dto.MemberDTO;
import project.board.admin.mapper.MemberMapper;
import project.board.admin.model.MemberParam;
import project.board.component.MailComponents;
import project.board.member.entity.LoginHistory;
import project.board.member.entity.Member;
import project.board.member.entity.MemberCode;
import project.board.member.exception.MemberNotEmailAuthException;
import project.board.member.exception.MemberStopUserException;
import project.board.member.model.LoginHistoryInput;
import project.board.member.model.ServiceResult;
import project.board.member.repository.LoginHistoryRepository;
import project.board.member.repository.MemberRepository;
import project.board.member.service.MemberService;
import project.board.member.model.MemberInput;
import project.board.member.model.ResetPasswordInput;
import project.board.util.PasswordUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MailComponents mailComponents;

    private final MemberMapper memberMapper;

    private final LoginHistoryRepository loginHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(BoardApplication.class);

    /**
     * 회원 가입
     */
    @Override
    public boolean register(MemberInput parameter) {

        // NullPointerException 방지
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()) {
            // 현재 userId에 해당하는 데이터 존재
            return false;
        }

        logger.info("=== register start ===");

        String encPassword = BCrypt.hashpw(parameter.getUserPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
            .userId(parameter.getUserId())
            .userPassword(encPassword)
            .userName(parameter.getUserName())
            .phone(parameter.getPhone())
            .userGender(parameter.getUserGender())
            .userEmail(parameter.getUserEmail())
            .regDt(LocalDateTime.now())
            .emailAuthYn(false)
            .emailAuthKey(uuid)
            .userStatus(Member.MEMBER_STATUS_REQ)
            .zipcode(parameter.getZipcode())
            .addr(parameter.getAddr())
            .addrDetail(parameter.getAddrDetail())
            .build();

        memberRepository.save(member);

        logger.info("=== register end ===");

        String email = parameter.getUserEmail();
        String subject = "SpringBoot 게시판 사이트 가입을 축하드립니다. ";
        String text = "<p>SpringBoot 게시판 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>"
            + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'>가입 완료</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 이메일 인증 통한 회원 활성화
     */
    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 재활성화 방지
        if (member.isEmailAuthYn()) {
            return false;
        }

        member.setUserStatus(Member.MEMBER_STATUS_ING);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    /**
     * 패스워드 초기화
     */
    @Override
    public boolean sendResetPassword(ResetPasswordInput parameter) {
        Optional<Member> optionalMember =
            memberRepository.findByUserEmail(parameter.getUserEmail());

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = parameter.getUserEmail();
        String subject = "[SpringBoot 게시판] 비밀번호 초기화 메일입니다. ";
        String text = "<p>SpringBoot 게시판 비밀번호 초기화 메일입니다.<p>" +
            "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요.</p>"
            + "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id="
            + uuid + "'>" +
            "비밀번호 초기화 링크</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 비밀번호 초기화 요청
     */
    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 체크
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setUserPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    /**
     * 비밀번호 재설정 후 다시 확인
     */
    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 초기화 날짜가 유효한지 체크
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(" 유효한 날짜가 아닙니다. ");
        }

        return true;
    }

    @Override
    public List<MemberDTO> list(MemberParam parameter) {

        long totalCount = memberMapper.selectListCount(parameter);
        List<MemberDTO> list = memberMapper.selectList(parameter);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDTO x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public MemberDTO detail(String userId) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();

        return MemberDTO.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {

        logger.info("=== updateStatus start ===");

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setUserStatus(userStatus);
        memberRepository.save(member);

        logger.info("=== updateStatus end ===");

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setUserPassword(encPassword);
        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult updateMember(MemberInput parameter) {

        logger.info("=== updateMember start ===");

        String userId = parameter.getUserId();

        LocalDateTime now = LocalDateTime.now();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setPhone(parameter.getPhone());
        member.setUdtDt(LocalDateTime.of(now.getYear(), now.getMonth(),
            now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()));
        member.setZipcode(parameter.getZipcode());
        member.setAddr(parameter.getAddr());
        member.setAddrDetail(parameter.getAddrDetail());
        memberRepository.save(member);

        logger.info("=== updateMember end ===");

        return new ServiceResult();
    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput parameter) {

        logger.info("=== updateMemberPassword start ===");

        String userId = parameter.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(parameter.getUserPassword(), member.getUserPassword())) {
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        String encPassword = PasswordUtils.encPassword(parameter.getNewPassword());
        member.setUserPassword(encPassword);
        memberRepository.save(member);

        logger.info("=== updateMemberPassword end ===");

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult withdraw(String userId, String userPassword) {

        logger.info("=== withdraw start ===");

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(userPassword, member.getUserPassword())) {
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        member.setUserName("삭제회원");
        member.setPhone("");
        member.setUserPassword("");
        member.setRegDt(null);
        member.setUdtDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey("");
        member.setResetPasswordKey("");
        member.setResetPasswordKey(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        member.setZipcode("");
        member.setAddr("");
        member.setAddrDetail("");
        memberRepository.save(member);

        logger.info("=== withdraw end ===");

        return new ServiceResult();
    }

    @Override
    public List<LoginHistoryDTO> loginHistory(String userId) {

        List<LoginHistoryDTO> loginHistoryList = new ArrayList<>();
        List<LoginHistory> loginHistories =
            loginHistoryRepository.findLoginHistoriesByUserId(userId);

        long count = loginHistories.size();
        for (LoginHistory x : loginHistories) {
            LoginHistoryDTO dto = LoginHistoryDTO.of(x);
            dto.setSeq(count);
            loginHistoryList.add(dto);
            count--;
        }

        return loginHistoryList;
    }


    @Override
    public boolean recordLoginHistory(LoginHistoryInput parameter) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        LoginHistory loginHistory = LoginHistory.builder()
            .userId(parameter.getUserId())
            .clientIp(parameter.getClientIp())
            .userAgent(parameter.getUserAgent())
            .loginDt(now)
            .build();

        loginHistoryRepository.save(loginHistory);

        Member member = optionalMember.get();
        member.setLastLoginDt(now);
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
        }

        if (Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())) {
            throw new MemberStopUserException("탈퇴된 회원 입니다.");
        }


        if (!member.isEmailAuthYn()) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인 해주세요");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 관리자 ROLE 추가
        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getUserPassword(), grantedAuthorities);
    }
}
