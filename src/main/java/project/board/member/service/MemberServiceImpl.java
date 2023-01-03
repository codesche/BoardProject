package project.board.member.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.board.entity.Member;
import project.board.member.repository.MemberRepository;
import project.board.model.MemberInput;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Override
    public boolean register(MemberInput parameter) {

        // NullPointerException
        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()) {
            // 현재 userId에 해당하는 데이터 존재
            return false;
        }

        Member member = new Member();
        member.setUserId(parameter.getUserId());
        member.setUserPassword(parameter.getUserPassword());
        member.setUserName(parameter.getUserName());
        member.setPhone(parameter.getPhone());
        member.setUserGender(parameter.getUserGender());
        member.setUserEmail(parameter.getUserEmail());
        member.setRegDt(LocalDateTime.now());

        memberRepository.save(member);

        return true;
    }
}
