package com.demo.cdmall1.domain.usedboard.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface UsedCommentRepository extends JpaRepository<UsedComment, Integer>{
	List<UsedComment> findByUsedBoardOrderByUbcnoDesc(UsedBoard build);
}
