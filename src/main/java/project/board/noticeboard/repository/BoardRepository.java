package project.board.noticeboard.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.board.entity.Member;
import project.board.noticeboard.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
