package project.board.noticeboard.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import project.board.noticeboard.dto.BoardDTO;
import project.board.noticeboard.model.BoardParam;

@Mapper
public interface BoardMapper {

    long selectListCount(BoardParam parameter);

    List<BoardDTO> selectList(BoardParam parameter);

    long countVisit(Long parameter);

}
