package project.board.noticeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.noticeboard.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
