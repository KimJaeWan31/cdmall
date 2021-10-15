package com.demo.cdmall1.domain.board.entity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberId>{

	boolean existsByBnoAndUsername(Integer bno, String loginId);

	void deleteByBnoAndUsernameAndKind(Integer bno, String loginId, String kind);

	BoardMember findByBnoAndUsername(Integer bno, String loginId);
}
