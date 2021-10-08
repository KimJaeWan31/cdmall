package com.demo.cdmall1.domain.imageboard.service;

import java.util.*;



import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.imageboard.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class IBCommentService {
	private final IBCommentRepository dao;
	
	public List<IBComment> write(IBCommentDto.Write dto, String profile, String loginId) {
		IBComment ibcomment = IBComment.builder().imageBoard(ImageBoard.builder().ibno(dto.getIbno()).build()).writer(loginId).content(dto.getContent()).profile(profile).build();
		dao.save(ibcomment);
		
		// stream을 사용하지 않는 이유 : stream은 읽기 전용
		List<IBComment> result = dao.findByImageBoardOrderByIbcnoDesc(ImageBoard.builder().ibno(dto.getIbno()).build());
		for(IBComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
		
		/* stream을 사용한다면....링크없는 Comment를 링크걸린 Comment로 변환(map)
		List<Comment> resultC1 = dao.findByBoardOrderByCnoDesc(Board.builder().bno(dto.getBno()).build());
		return resultC1.stream().map(c1->new Comment(c1.getCno(), c1.getBoard(), c1.getWriter(), c1.getContent(), ZmallConstant.PROFILE_URL + c1.getProfile())).collect(Collectors.toList());
		*/
	}

	public List<IBComment> delete(IBCommentDto.Delete dto, String loginId) {
		System.out.println(dto);
		IBComment ibcomment = dao.findById(dto.getIbcno()).orElseThrow(BoardFail.CommentNotFoundException::new);
		if(ibcomment.getWriter().equals(loginId)==false) 
			throw new BoardFail.IllegalJobException();
		dao.delete(ibcomment);
		
		List<IBComment> result = dao.findByImageBoardOrderByIbcnoDesc(ImageBoard.builder().ibno(dto.getIbno()).build());
		for(IBComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
	}
}
