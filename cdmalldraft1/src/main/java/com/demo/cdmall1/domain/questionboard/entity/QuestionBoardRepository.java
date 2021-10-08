package com.demo.cdmall1.domain.questionboard.entity;

import org.springframework.data.jpa.repository.*;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Integer>, QuestionBoardCustomRepository{

}
