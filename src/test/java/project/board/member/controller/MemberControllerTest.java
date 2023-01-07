package project.board.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.board.entity.Member;
import project.board.member.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 가입 성공")
    @Test
    void register() {
        // given
        Member member = Member.builder()
            .userId("minsung")
            .userPassword("1111")
            .phone("010-2222-3333")
            .userGender("male")
            .userEmail("hms@naver.com")
            .regDt(LocalDateTime.now())
            .build();

        // when
        Member result = memberRepository.save(member);

        // then
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getUserPassword()).isEqualTo("1111");
        assertThat(result.getPhone()).isEqualTo("010-2222-3333");
        assertThat(result.getUserGender()).isEqualTo("male");
        assertThat(result.getUserEmail()).isEqualTo("hms@naver.com");
        assertThat(result.getRegDt().isEqual(LocalDateTime.now()));
    }

    @Test
    void registerSubmit() {
    }
}