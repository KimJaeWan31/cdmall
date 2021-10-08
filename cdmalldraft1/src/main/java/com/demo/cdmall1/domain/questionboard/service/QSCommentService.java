package com.demo.cdmall1.domain.questionboard.service;

import java.util.*;


import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class QSCommentService {
	private final QSCommentRepository dao;
	
	public List<QSComment> write(QSCommentDto.Write dto, String profile, String loginId) {
		QSComment qscomment = QSComment.builder().questionBoard(QuestionBoard.builder().qbno(dto.getQbno()).build()).writer(loginId).content(dto.getContent()).profile(profile).build();
		dao.save(qscomment);
		
		// stream을 사용하지 않는 이유 : stream은 읽기 전용
		List<QSComment> result = dao.findByQuestionBoardOrderByQbcnoDesc(QuestionBoard.builder().qbno(dto.getQbno()).build());
		for(QSComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
		
		/* stream을 사용한다면....링크없는 Comment를 링크걸린 Comment로 변환(map)
		List<Comment> resultC1 = dao.findByBoardOrderByCnoDesc(Board.builder().bno(dto.getBno()).build());
		return resultC1.stream().map(c1->new Comment(c1.getCno(), c1.getBoard(), c1.getWriter(), c1.getContent(), ZmallConstant.PROFILE_URL + c1.getProfile())).collect(Collectors.toList());
		*/
	}

	public List<QSComment> delete(QSCommentDto.Delete dto, String loginId) {
		QSComment qscomment = dao.findById(dto.getQbcno()).orElseThrow(BoardFail.CommentNotFoundException::new);
		if(qscomment.getWriter().equals(loginId)==false) 
			throw new BoardFail.IllegalJobException();
		dao.delete(qscomment);
		
		List<QSComment> result = dao.findByQuestionBoardOrderByQbcnoDesc(QuestionBoard.builder().qbno(dto.getQbno()).build());
		for(QSComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
	}
}
