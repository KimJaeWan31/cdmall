package com.demo.cdmall1.domain.customercenter.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.board.entity.Board;
import com.demo.cdmall1.domain.board.entity.BoardCustomRepository;
import com.demo.cdmall1.domain.board.entity.QBoard;
import com.demo.cdmall1.domain.customercenter.entity.*;
import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.BoardDto.WarnList;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class VocBoardCustomRepositoryImpl extends QuerydslRepositorySupport implements VocBoardCustomRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	
	public VocBoardCustomRepositoryImpl() {
		super(VocBoard.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
	}
	
	// select * from board where bno>0;
		@Override
		public List<VocBoardDto.List> readAll(Pageable pageable) {
			QVocBoard vocBoard = QVocBoard.vocBoard;
			return factory.from(vocBoard).select(Projections.constructor(VocBoardDto.List.class, vocBoard.vbno, vocBoard.title, vocBoard.writer, 
					vocBoard.createTime, vocBoard.readCnt, vocBoard.attachmentCnt, vocBoard.commentCnt)).where(vocBoard.vbno.gt(0))
					.orderBy(vocBoard.vbno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		}

		//
		@Override
		public Long countByVbno() {
			QVocBoard vocBoard = QVocBoard.vocBoard;
			return factory.from(vocBoard).select(vocBoard.vbno.count()).where(vocBoard.vbno.gt(0)).fetchOne();
		}
	}

	















