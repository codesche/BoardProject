package project.board.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.board.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
}
