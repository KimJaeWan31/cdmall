package com.demo.cdmall1.domain.questionboard.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.QuestionBoardDto.*;
import com.demo.cdmall1.domain.questionboard.entity.QQuestionBoard;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class QuestionBoardCustomRepositoryImpl extends QuerydslRepositorySupport implements QuestionBoardCustomRepository{
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QQuestionBoard qboard;
	
	public QuestionBoardCustomRepositoryImpl() {
		super(QuestionBoard.class);
	}
	
	@PostConstruct
	public void init() {

		this.factory = new JPAQueryFactory(em);
		qboard = QQuestionBoard.questionBoard;
	}

	@Override
	public List<ListView> readAll(Pageable pageable, String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qboard.qbno.gt(0));
		if(writer!=null)
			condition.and(qboard.writer.eq(writer));
		return factory.from(qboard).select(Projections.constructor(QuestionBoardDto.ListView.class, qboard.qbno, qboard.title, qboard.writer, 
				qboard.createTime, qboard.readCnt, qboard.attachmentCnt, qboard.commentCnt)).where(condition)
				.orderBy(qboard.qbno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	@Override
	public Long countAll(String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qboard.qbno.gt(0));
		if(writer!=null)
			condition.and(qboard.writer.eq(writer));
		
		return factory.from(qboard).select(qboard.qbno.count()).where(condition).fetchOne();
	}


}
