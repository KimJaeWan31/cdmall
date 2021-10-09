package com.demo.cdmall1.domain.chatting.entity.impl;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.*;

import com.demo.cdmall1.domain.chatting.entity.ChattingCustomRepository;
import com.demo.cdmall1.domain.chatting.entity.ChattingHistory;
import com.demo.cdmall1.domain.chatting.entity.QChattingHistory;
import com.demo.cdmall1.web.dto.ChattingHistoryDto;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

public class ChattingCustomRepositoryImpl extends QuerydslRepositorySupport implements ChattingCustomRepository {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory factory;
	private QChattingHistory chattingHistory;
	
	public ChattingCustomRepositoryImpl() {
		super(ChattingHistory.class);
	}
	
	@PostConstruct
	public void init() {
		this.factory = new JPAQueryFactory(em);
		chattingHistory = QChattingHistory.chattingHistory;
	}

	// select * from board where bno>0;
	@Override
	public List<ChattingHistoryDto.ListView> readAll(Pageable pageable, String guestid) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(chattingHistory.chathno.gt(0));
		if(guestid!=null) {
			condition.and(chattingHistory.guestid.eq(guestid));
		}
		return factory.from(chattingHistory)
				.select(Projections
						.constructor(ChattingHistoryDto.ListView.class,
								chattingHistory.chathno, chattingHistory.createdTime,
								chattingHistory.guestid, chattingHistory.hostid))
				.where(condition)
				.orderBy(chattingHistory.chathno.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	//
	@Override
	public Long countAll(String guestid) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(chattingHistory.chathno.gt(0));
		if(guestid!=null) {
			condition.and(chattingHistory.guestid.eq(guestid));
		}
		return factory
				.from(chattingHistory)
				.select(chattingHistory.chathno.count())
				.where(condition)
				.fetchOne();
	}

}



















