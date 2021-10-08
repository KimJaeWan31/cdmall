package com.demo.cdmall1.domain.board.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findByBoardOrderByCnoDesc(Board build);

}
