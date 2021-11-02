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
import com.demo.cdmall1.web.dto.BoardDto.WarnList;
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
	/*
	 * @Override public List<BoardDto.ListView> readAll(Pageable pageable, String
	 * writer) { BooleanBuilder condition = new BooleanBuilder();
	 * condition.and(board.bno.gt(0)); if(writer!=null)
	 * condition.and(board.writer.eq(writer)); return
	 * factory.from(board).select(Projections.constructor(BoardDto.ListView.class,
	 * board.bno, board.title, board.writer, board.createTime, board.readCnt,
	 * board.attachmentCnt, board.commentCnt)).where(condition)
	 * .orderBy(board.bno.desc()).offset(pageable.getOffset()).limit(pageable.
	 * getPageSize()).fetch(); }
	 * 
	 * //
	 * 
	 * @Override public Long countAll(String writer) { BooleanBuilder condition =
	 * new BooleanBuilder(); condition.and(board.bno.gt(0)); if(writer!=null)
	 * condition.and(board.writer.eq(writer));
	 * 
	 * return
	 * factory.from(board).select(board.bno.count()).where(condition).fetchOne(); }
	 */
	
	@Override
	public List<BoardDto.List> readAll(Pageable pageable, String writer, String category) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(writer!=null)
			condition.and(board.writer.eq(writer));
		if(category!=null)
			condition.and(board.category.eq(category));
		return factory.from(board).select(Projections.constructor(BoardDto.List.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt, board.goodCnt, board.badCnt, board.category, board.warnCnt)).where(condition)
				.orderBy(board.bno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}
	
	// geo(>=) gt(=)
	@Override
	public List<BoardDto.BestList> readBestAll(Pageable pageable, Integer goodCnt) {
		QBoard board = QBoard.board;
		return factory.from(board).select(Projections.constructor(BoardDto.BestList.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt, board.goodCnt, board.badCnt, board.category, board.warnCnt))
				.where(board.bno.gt(0).and(board.goodCnt.goe(10)))
				.orderBy(board.goodCnt.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	
	@Override
	public List<BoardDto.BestRank> readBestRank(Pageable pageable, Integer goodCnt){
		QBoard board = QBoard.board;
		return factory.from(board).select(Projections.constructor(BoardDto.BestRank.class, board.bno, board.title, board.writer,
				board.commentCnt, board.category))
				.where(board.bno.gt(0))
	.orderBy(board.goodCnt.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}
	
	@Override
	public Long countByGoodCnt2(){
		QBoard board = QBoard.board;
		return factory.from(board).select(board.bno.count()).where(board.bno.gt(0)).limit(3).fetchOne();
	}
	
	@Override
	public Long countByBno() {
		QBoard board = QBoard.board;
		return factory.from(board).select(board.bno.count()).where(board.bno.gt(0)).fetchOne();
	}
	
	@Override
	public Long countAll(String writer, String category) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(writer!=null)
			condition.and(board.writer.eq(writer));
		if(category!=null)
			condition.and(board.category.eq(category));
		return factory.from(board).select(board.bno.count()).where(condition).fetchOne();
	}
	
	@Override
	public Long countByGoodCnt() {
		QBoard board = QBoard.board;
		return factory.from(board).select(board.bno.count()).where(board.bno.gt(0).and(board.goodCnt.goe(10))).fetchOne();
	}

	@Override
	public List<WarnList> readWarnAll(Pageable pageable, Integer warnCnt) {
		QBoard board = QBoard.board;
		return factory.from(board).select(Projections.constructor(BoardDto.WarnList.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt, board.goodCnt,board.badCnt, board.category, board.warnCnt, board.isActive))
				.where(board.bno.gt(0).and(board.warnCnt.goe(1)))
				.orderBy(board.warnCnt.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	@Override
	public Long countByWarnCnt() {
		QBoard board = QBoard.board;
		return factory.from(board).select(board.bno.count()).where(board.bno.gt(0).and(board.warnCnt.goe(0))).fetchOne();
	}
	
	// 검색
	@Override
	public List<BoardDto.List> search(Pageable pageable, String word){
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(word!=null) 
			condition.and(board.title.contains(word).or(board.content.contains(word)).or(board.writer.contains(word)));
		return factory.from(board).select(Projections.constructor(BoardDto.List.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt, board.goodCnt, board.badCnt, board.category, board.warnCnt))
				.where(condition).orderBy(board.bno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		
	}
	
	@Override
	public Long countSearch(String word) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(word!=null) 
			condition.and(board.title.contains(word).or(board.content.contains(word)).or(board.writer.contains(word)));
		return factory.from(board).select(board.bno.count()).where(condition).fetchOne();
	}
	
	
	//추천게시판 검색
	@Override
	public List<BoardDto.BestList> searchBest(Pageable pageable, String word){
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(word!=null) 
			condition.and(board.title.contains(word).or(board.content.contains(word)).or(board.writer.contains(word)));
		return factory.from(board).select(Projections.constructor(BoardDto.BestList.class, board.bno, board.title, board.writer, 
				board.createTime, board.readCnt, board.attachmentCnt, board.commentCnt, board.goodCnt, board.badCnt, board.category, board.warnCnt))
				.where(condition.and(board.goodCnt.goe(10))).orderBy(board.bno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}
	
	
	@Override
	public Long countSearchBest(String word) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(board.bno.gt(0));
		if(word!=null) 
			condition.and(board.title.contains(word).or(board.content.contains(word)).or(board.writer.contains(word)));
		return factory.from(board).select(board.bno.count()).where(condition.and(board.goodCnt.goe(10))).fetchOne();
	}
}



















