package com.demo.cdmall1.domain.questionboard.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface QSCommentRepository extends JpaRepository<QSComment, Integer>{
	List<QSComment> findByQuestionBoardOrderByQbcnoDesc(QuestionBoard build);
}
