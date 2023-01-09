package project.board.member.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import project.board.component.MailComponents;
import project.board.entity.Member;
import project.board.member.exception.MemberNotEmailAuthException;
import project.board.member.repository.MemberRepository;
import project.board.member.service.MemberService;
import project.board.model.MemberInput;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MailComponents mailComponents;

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
            .build();

        memberRepository.save(member);

        String email = parameter.getUserEmail();
        String subject = "fastlms 사이트 가입을 축하드립니다. ";
        String text = "<p>fastlms 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>"
            + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'>가입 완료</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

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

        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!member.isEmailAuthYn()) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인 해주세요");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(member.getUserId(), member.getUserPassword(), grantedAuthorities);
    }
}
