package com.demo.cdmall1.memo.service;

import java.util.*;

import javax.annotation.*;
import javax.persistence.*;

import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.domain.memo.entity.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.domain.member.entity.QMember;
import com.demo.cdmall1.domain.memo.entity.QMemo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemoService {
	private final EntityManager em;
	private JPAQueryFactory factory;
	private QMemo memo;
	
	@PostConstruct
	public void init() {
		factory = new JPAQueryFactory(em);
		memo = QMemo.memo;
	}

	/* member에 대한 exists 쿼리 확인
	public Boolean exists(String loginId) {
		QMember member = QMember.member;
		// selectOne -> select 1
		// fetchFirst -> rownum<=1
		Integer result = factory.selectOne().from(member).where(member.username.eq(loginId)).fetchFirst();
		return result!=null;
	}
	*/
	
	// write - QueryDsl은 save를 지원하지 않는다. EntityManager로 직접 저장한다
	@Transactional
	public Memo write(MemoDto.Write dto, String loginId) {
		QMember member = QMember.member;
		Integer result = factory.selectOne().from(member).where(member.username.eq(dto.getReceiver())).fetchFirst();
		if(result==null)
			throw new MemberFail.MemberNotFoundException();
		Memo param = dto.toEntity();
		param.setSender(loginId);
		em.persist(param);
		em.flush();
		return param;
	}
	
	// 받은 목록
	// 번호, 제목, 내용, 보낸사람, 작성시간, 읽기여부
	public List<Memo> receiveList1(String loginId) {
		List<Tuple> result = factory.from(memo).select(memo.mno, memo.title, memo.content, memo.sender, memo.isRead)
				.where(memo.receiver.eq(loginId)).fetch();
		List<Memo> list = new ArrayList<>();
		result.forEach(tuple->{
			Memo memo = new Memo();
			memo.setMno(tuple.get(0, Integer.class));
			memo.setTitle(tuple.get(1, String.class));
			memo.setContent(tuple.get(2, String.class));
			memo.setSender(tuple.get(3, String.class));
			memo.setIsRead(tuple.get(4, Boolean.class));
			list.add(memo);
		});
		return list;
	}
	
	public List<MemoDto.ReceiveList> receiveList(String loginId) {
		return factory.from(memo)
				.select(Projections.constructor(MemoDto.ReceiveList.class, memo.mno, memo.title, memo.content, memo.sender, memo.createTime, memo.isRead))
				.where(memo.receiver.eq(loginId).and(memo.disabledByReceiver.eq(false)).and(memo.mno.gt(0))).orderBy(memo.mno.desc()).fetch();
	}
	
	
	// 보낸 목록
	// 번호, 제목, 내용, 받은사람, 작성시간, 읽기여부
	public List<MemoDto.SendList> sendList(String loginId) {
		return factory.from(memo)
				.select(Projections.constructor(MemoDto.SendList.class, memo.mno, memo.title, memo.content, memo.receiver, memo.createTime, memo.isRead))
				.where(memo.sender.eq(loginId).and(memo.disabledBySender.eq(false)).and(memo.mno.gt(0))).orderBy(memo.mno.desc()).fetch();
	}
	
	// 읽기 : 글번호
	@Transactional
	public Memo read(Integer mno) {
		Memo result = factory.selectFrom(memo).where(memo.mno.eq(mno)).fetchOne();
		if(result==null)
			throw new MemoNotFoundException();
		if(result.getIsRead()==false)
			result.setIsRead(true);
		return result;
	}
	
	
	// 받은쪽에서 비활성화
	@Transactional
	public long disabledBySender(List<Integer> mnos) {
		// update memo set 어쩌구 where mno in (11,22,33);
		// delete, update는 execute로 실행한다
		return factory.update(memo).where(memo.mno.in(mnos)).set(memo.disabledBySender, true).execute();
	}
	
	// 보낸쪽에서 비활성화
	@Transactional
	public long disabledByReceiver(List<Integer> mnos) {
		return factory.update(memo).where(memo.mno.in(mnos)).set(memo.disabledByReceiver, true).execute();
	}
	
	// 삭제 : disableBySender, disabledByReceiver 모두 true가 되면 삭제
	@Scheduled(cron="0 0 4 1/1 * ?")
	@Transactional
	public long delete() {
		return factory.delete(memo).where(memo.disabledByReceiver.eq(true).and(memo.disabledBySender.eq(true))).execute();
	}

	public long unreadMemo(String loginId) {
		return factory.select(memo.mno).from(memo).where(memo.receiver.eq(loginId).and(memo.disabledByReceiver.eq(false))).fetchCount();
	}
}


















