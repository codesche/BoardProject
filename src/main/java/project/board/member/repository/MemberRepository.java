package project.board.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.board.member.entity.Member;


public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);

    Optional<Member> findByUserEmail(String email);

    Optional<Member> findByResetPasswordKey(String resetPasswordKey);

//    // 회원 존재 여부 테스트 위해 생성
//    Member findByUserId(String id);


}
