package project.board.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import project.board.admin.dto.MemberDTO;
import project.board.admin.model.MemberParam;

@Mapper
public interface MemberMapper {

    long selectListCount(MemberParam parameter);

    List<MemberDTO> selectList(MemberParam parameter);

}
