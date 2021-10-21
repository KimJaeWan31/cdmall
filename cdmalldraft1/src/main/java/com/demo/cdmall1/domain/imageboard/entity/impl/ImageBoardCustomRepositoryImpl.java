package com.demo.cdmall1.domain.imageboard.entity.impl;

import java.util.List;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.imageboard.entity.ImageBoard;
import com.demo.cdmall1.domain.imageboard.entity.ImageBoardCustomRepository;
import com.demo.cdmall1.domain.imageboard.entity.QImageBoard;
import com.demo.cdmall1.web.dto.ImageBoardDto;
import com.demo.cdmall1.web.dto.ImageBoardDto.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class ImageBoardCustomRepositoryImpl extends QuerydslRepositorySupport implements ImageBoardCustomRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	
	public ImageBoardCustomRepositoryImpl() {
		super(ImageBoard.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
	}

	// select * from board where bno>0;
	@Override
	public List<ImageBoardDto.List> readAll(Pageable pageable) {
		QImageBoard imageBoard = QImageBoard.imageBoard;
		return factory.from(imageBoard).select(Projections.constructor(ImageBoardDto.List.class, imageBoard.ibno, imageBoard.title, imageBoard.writer,
				imageBoard.imageFileName, imageBoard.createTime, imageBoard.goodCnt, imageBoard.reportCnt)).where(imageBoard.ibno.gt(0))
				.orderBy(imageBoard.ibno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	//
	@Override
	public Long countByIbno() {
		QImageBoard imageBoard = QImageBoard.imageBoard;
		return factory.from(imageBoard).select(imageBoard.ibno.count()).where(imageBoard.ibno.gt(0)).fetchOne();
	}

	@Override
	public List<ReportList> readReportAll(Pageable pageable, Integer reportCnt) {
		QImageBoard imageBoard = QImageBoard.imageBoard;
		return factory.from(imageBoard).select(Projections.constructor(ImageBoardDto.ReportList.class, imageBoard.ibno, imageBoard.title, imageBoard.writer, 
				imageBoard.createTime, imageBoard.goodCnt, imageBoard.reportCnt))
				.where(imageBoard.ibno.gt(0).and(imageBoard.reportCnt.goe(1)))
				.orderBy(imageBoard.reportCnt.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}

	@Override
	public Long countByReportCnt() {
		QImageBoard imageBoard = QImageBoard.imageBoard;
		return factory.from(imageBoard).select(imageBoard.ibno.count()).where(imageBoard.ibno.gt(0).and(imageBoard.reportCnt.goe(10))).fetchOne();
	}
}
