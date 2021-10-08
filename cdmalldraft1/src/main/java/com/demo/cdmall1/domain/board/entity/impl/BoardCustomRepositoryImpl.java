package com.demo.cdmall1.domain.board.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.board.entity.Board;
import com.demo.cdmall1.domain.board.entity.BoardCustomRepository;
import com.demo.cdmall1.domain.board.entity.QBoard;
import com.demo.cdmall1.web.dto.*;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class BoardCustomRepositoryImpl extends QuerydslRepositorySupport implements BoardCustomRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QBoard board;
	
	public BoardCustomRepositoryImpl() {
		super(Board.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		board = QBoard.board;
	}

	// select * from board where bno>0;
	@Override
	public List<BoardDto.ListView> readAll(Pageable pageable, String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(writer!=null)
			condition.and(board.writer.eq(writer));
		return factory.from(board).select(Projections.constructor(BoardDto.ListView.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt)).where(condition)
				.orderBy(board.bno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	//
	@Override
	public Long countAll(String writer) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(writer!=null)
			condition.and(board.writer.eq(writer));
		
		return factory.from(board).select(board.bno.count()).where(condition).fetchOne();
	}

}



















