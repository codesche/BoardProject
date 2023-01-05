package project.board.member.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.board.component.MailComponents;
import project.board.entity.Member;
import project.board.member.repository.MemberRepository;
import project.board.member.service.MemberService;
import project.board.model.MemberInput;

@RequiredArgsConstructor
@Service
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

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
            .userId(parameter.getUserId())
            .userPassword(parameter.getUserPassword())
            .userName(parameter.getUserName())
            .phone(parameter.getPhone())
            .userGender(parameter.getUserGender())
            .userEmail(parameter.getUserEmail())
            .regDt(LocalDateTime.now())
            .emailAuthYn(false)
            .emailAuthKey(uuid)
            .build();

//        member.setUserId();
        member.setUserPassword(parameter.getUserPassword());
        member.setUserName(parameter.getUserName());
        member.setPhone(parameter.getPhone());
        member.setUserGender(parameter.getUserGender());
        member.setUserEmail(parameter.getUserEmail());
        member.setRegDt(LocalDateTime.now());

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

//        member.setUserStatus(MemberCode.MEMBER_STATUS_ING);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }
}
