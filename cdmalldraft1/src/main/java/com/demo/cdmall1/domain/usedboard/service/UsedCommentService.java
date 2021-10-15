package com.demo.cdmall1.domain.usedboard.service;

import java.util.*;


import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedComment;
import com.demo.cdmall1.domain.usedboard.entity.UsedCommentDto;
import com.demo.cdmall1.domain.usedboard.entity.UsedCommentRepository;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class UsedCommentService {
	private final UsedCommentRepository dao;
	
	public List<UsedComment> write(UsedCommentDto.Write dto, String profile, String loginId) {
		UsedComment usedcomment = UsedComment.builder().usedBoard(UsedBoard.builder().ubno(dto.getUbno()).build()).writer(loginId).content(dto.getContent()).profile(profile).build();
		dao.save(usedcomment);
		
		// stream을 사용하지 않는 이유 : stream은 읽기 전용
		List<UsedComment> result = dao.findByUsedBoardOrderByUbcnoDesc(UsedBoard.builder().ubno(dto.getUbno()).build());
		for(UsedComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
		
		/* stream을 사용한다면....링크없는 Comment를 링크걸린 Comment로 변환(map)
		List<Comment> resultC1 = dao.findByBoardOrderByCnoDesc(Board.builder().bno(dto.getBno()).build());
		return resultC1.stream().map(c1->new Comment(c1.getCno(), c1.getBoard(), c1.getWriter(), c1.getContent(), ZmallConstant.PROFILE_URL + c1.getProfile())).collect(Collectors.toList());
		*/
	}

	public List<UsedComment> delete(UsedCommentDto.Delete dto, String loginId) {
		UsedComment usedcomment = dao.findById(dto.getUbcno()).orElseThrow(BoardFail.CommentNotFoundException::new);
		if(usedcomment.getWriter().equals(loginId)==false) 
			throw new BoardFail.IllegalJobException();
		dao.delete(usedcomment);
		
		List<UsedComment> result = dao.findByUsedBoardOrderByUbcnoDesc(UsedBoard.builder().ubno(dto.getUbno()).build());
		for(UsedComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
	}
}
