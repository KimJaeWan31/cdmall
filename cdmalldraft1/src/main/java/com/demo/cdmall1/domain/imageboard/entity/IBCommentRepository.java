package com.demo.cdmall1.domain.imageboard.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface IBCommentRepository extends JpaRepository<IBComment, Integer>{

	List<IBComment> findByImageBoardOrderByIbcnoDesc(ImageBoard build);

}
