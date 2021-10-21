package com.demo.cdmall1.domain.product.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.ProductDto.*;
import com.demo.cdmall1.domain.product.entity.QProduct;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class ProductCustomRepositoryImpl extends QuerydslRepositorySupport implements ProductCustomRepository{
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QProduct qproduct;
	
	
	public ProductCustomRepositoryImpl() {
		super(Product.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		qproduct = QProduct.product;
	}
	
	@Override
	public List<ProductDto.ProductList> search(Pageable pageable, String word){
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qproduct.pno.gt(0));
		if(word!=null)
			condition.and(qproduct.name.contains(word).or(qproduct.manufacturer.contains(word)));
		return factory.from(qproduct).select(Projections.constructor(ProductDto.ProductList.class, 
				qproduct.pno, qproduct.manufacturer, qproduct.name, qproduct.image, qproduct.price, qproduct.avgOfStar, qproduct.reviewCount, qproduct.imageFileName, qproduct.goodCnt, qproduct.goodCnlCnt))
				.where(condition).orderBy(qproduct.pno.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		
	}
	
	@Override
	public Long countAll(String word) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(qproduct.pno.gt(0));
		if(word!=null)
			condition.and(qproduct.name.contains(word).or(qproduct.manufacturer.contains(word)));
		
		return factory.from(qproduct).select(qproduct.pno.count()).where(condition).fetchOne();
	}
}
