package com.demo.cdmall1.domain.board.entity;

import org.springframework.data.repository.*;

public interface BoardMemberRepository extends CrudRepository<BoardMember, BoardMemberId>{

	boolean existsByBnoAndUsername(Integer bno, String loginId);

}
