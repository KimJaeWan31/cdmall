package com.demo.cdmall1.domain.noticeboard.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.domain.noticeboard.entity.QNoticeBoard;
import com.demo.cdmall1.domain.usedboard.entity.*;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class NoticeBoardCustomRepositoryImpl extends QuerydslRepositorySupport implements NoticeBoardCustomRepository{
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	
	public NoticeBoardCustomRepositoryImpl() {
		super(NoticeBoard.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
	}

	// select * from board where bno>0;
	@Override
	public List<com.demo.cdmall1.web.dto.NoticeBoardDto.List> readAll(Pageable pageable) {
		QNoticeBoard noticeBoard = QNoticeBoard.noticeBoard;
		return factory.from(noticeBoard).select(Projections.constructor(NoticeBoardDto.List.class, noticeBoard.nbno, noticeBoard.title, noticeBoard.writer, 
				noticeBoard.createTime,noticeBoard.readCnt)).where(noticeBoard.nbno.gt(0))
				.orderBy(noticeBoard.nbno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	//
	@Override
	public Long countByNbno() {
		QNoticeBoard noticeBoard = QNoticeBoard.noticeBoard;
		return factory.from(noticeBoard).select(noticeBoard.nbno.count()).where(noticeBoard.nbno.gt(0)).fetchOne();
	}
	
	
	// 검색
	public List<com.demo.cdmall1.web.dto.NoticeBoardDto.List> search(Pageable pageable, String word){
		QNoticeBoard noticeBoard = QNoticeBoard.noticeBoard;
		BooleanBuilder conditions = new BooleanBuilder();
		conditions.and(noticeBoard.nbno.gt(0));
		if(word!=null) 
			conditions.and(noticeBoard.title.contains(word).or(noticeBoard.writer.contains(word)));
		return factory.from(noticeBoard).select(Projections.constructor(NoticeBoardDto.List.class, noticeBoard.nbno, noticeBoard.title, noticeBoard.writer, 
				noticeBoard.createTime,noticeBoard.readCnt)).where(conditions)
				.orderBy(noticeBoard.nbno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}
		
	public Long countSearch(String word) {
		QNoticeBoard noticeBoard = QNoticeBoard.noticeBoard;
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(noticeBoard.nbno.gt(0));
		if(word!=null) 
			condition.and(noticeBoard.title.contains(word).or(noticeBoard.writer.contains(word)));
		return factory.from(noticeBoard).select(noticeBoard.nbno.count()).where(condition).fetchOne();
	}
}
