package com.demo.cdmall1.domain.usedboard.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.domain.usedboard.entity.QUsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardCustomRepository;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardDto;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardDto.ListView;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class UsedBoardCustomRepositoryImpl extends QuerydslRepositorySupport implements UsedBoardCustomRepository{
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QUsedBoard qusedBoard;
	
	public UsedBoardCustomRepositoryImpl() {
		super(UsedBoard.class);
	}
	
	@PostConstruct
	public void init() {

		this.factory = new JPAQueryFactory(em);
		qusedBoard = QUsedBoard.usedBoard;
	}
	
	
	@Override
	public List<ListView> readAll(Pageable pageable, String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qusedBoard.ubno.gt(0));
		if(writer!=null)
			condition.and(qusedBoard.writer.eq(writer));
		return factory.from(qusedBoard).select(Projections.constructor(UsedBoardDto.ListView.class, qusedBoard.ubno, qusedBoard.title, qusedBoard.writer, 
				qusedBoard.createTime, qusedBoard.readCnt, qusedBoard.attachmentCnt, qusedBoard.commentCnt)).where(condition)
				.orderBy(qusedBoard.ubno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	@Override
	public Long countAll(String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qusedBoard.ubno.gt(0));
		if(writer!=null)
			condition.and(qusedBoard.writer.eq(writer));
		
		return factory.from(qusedBoard).select(qusedBoard.ubno.count()).where(condition).fetchOne();
	}


}
