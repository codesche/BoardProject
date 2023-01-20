package project.board.member.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.board.member.entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, String> {

    List<LoginHistory> findLoginHistoriesByUserId(String userId);

}
