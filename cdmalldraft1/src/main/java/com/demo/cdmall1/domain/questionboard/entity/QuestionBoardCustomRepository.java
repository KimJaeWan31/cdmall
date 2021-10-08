package com.demo.cdmall1.domain.questionboard.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface QuestionBoardCustomRepository {
	List<QuestionBoardDto.ListView> readAll(Pageable pageable,String writer);
	
	public Long countAll(String writer);
}
