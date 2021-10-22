package com.demo.cdmall1.domain.usedboard.entity.impl;

import java.util.List;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.usedboard.entity.QUsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardCustomRepository;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardDto;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardDto.*;
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
				qusedBoard.createTime, qusedBoard.readCnt, qusedBoard.attachmentCnt, qusedBoard.commentCnt, qusedBoard.warnCnt)).where(condition)
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

	@Override
	public List<WarnList> readWarnAll(Pageable pageable, Integer warnCnt) {
		QUsedBoard qusedBoard = QUsedBoard.usedBoard;
		return factory.from(qusedBoard).select(Projections.constructor(UsedBoardDto.WarnList.class, qusedBoard.ubno, qusedBoard.title, qusedBoard.writer, 
				qusedBoard.createTime, qusedBoard.readCnt, qusedBoard.attachmentCnt, qusedBoard.commentCnt, qusedBoard.warnCnt))
				.where(qusedBoard.ubno.gt(0).and(qusedBoard.warnCnt.goe(1)))
				.orderBy(qusedBoard.warnCnt.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	
	}

	@Override
	public Long countByWarnCnt() {
		QUsedBoard qusedBoard = QUsedBoard.usedBoard;
		return factory.from(qusedBoard).select(qusedBoard.ubno.count()).where(qusedBoard.ubno.gt(0).and(qusedBoard.warnCnt.goe(10))).fetchOne();
	}


}
