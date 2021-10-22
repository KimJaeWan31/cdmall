package com.demo.cdmall1.domain.usedboard.entity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsedBoardMemberRepository extends JpaRepository<UsedBoardMember, UsedBoardMemberId>{

	boolean existsByUbnoAndUsername(Integer ubno, String loginId);

	//void deleteByUbnoAndUsernameAndKind(Integer ubno, String loginId, String kind);

	UsedBoardMember findByUbnoAndUsername(Integer ubno, String loginId);
}
