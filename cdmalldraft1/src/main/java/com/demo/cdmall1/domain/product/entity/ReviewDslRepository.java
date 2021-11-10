package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.ReviewDto.ReviewList;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

@Repository
public class ReviewDslRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QReview qreview;

	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		qreview = QReview.review;
	}

	// select * from board where bno>0;
	public List<ReviewList> readAll(Pageable pageable, Product product) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qreview.rno.gt(0));
		if (product != null) {
			condition.and(qreview.product.pno.eq(product.getPno()));
		} 
		
		return factory.from(qreview)
				.select(Projections.constructor(ReviewDto.ReviewList.class, qreview.rno, qreview.star,
						qreview.content, qreview.writer, qreview.createTime, qreview.readCnt, qreview.goodCnt,
						qreview.badCnt, qreview.warnCnt))
				.where(condition).orderBy(qreview.createTime.desc()).offset(pageable.getOffset())
				.limit(pageable.getPageSize()).fetch();
	}
	
	public Long countByPno(Product product) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qreview.rno.gt(0));
		
		if (product != null) {
			condition.and(qreview.product.pno.eq(product.getPno()));
			
		} 
		 

		return factory.from(qreview).select(qreview.rno.count()).where(condition).fetchOne();
	}
	
	/*
	 * public List<StarList> readStarByPno(Product product){ BooleanBuilder
	 * condition = new BooleanBuilder(); condition.and(qreview.rno.gt(0));
	 * if(product != null) {
	 * condition.and(qreview.product.pno.eq(product.getPno())); }
	 * 
	 * return factory.from(qreview)
	 * .select(Projections.constructor(ReviewDto.StarList.class, qreview.star))
	 * .where(condition).fetch(); }
	 */
	
	public List<Integer> readStarByPno(Product product){
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qreview.rno.gt(0));
		if(product != null) {
			condition.and(qreview.product.pno.eq(product.getPno()));
		}
		
		return factory.from(qreview)
				.select(qreview.star)
				.where(condition).fetch();
	}
	
	/*
	 * public List<ReviewList> countByPno(Product product) { BooleanBuilder
	 * condition = new BooleanBuilder(); condition.and(qreview.rno.gt(0));
	 * 
	 * if (product != null) {
	 * condition.and(qreview.product.pno.eq(product.getPno()));
	 * 
	 * }
	 * 
	 * 
	 * return
	 * factory.from(qreview).select(qreview.rno.count()).where(condition).fetchOne()
	 * ; }
	 */
	
	

}
