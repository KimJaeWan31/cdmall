package com.demo.cdmall1.domain.customercenter.service;

import java.util.*;

import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.customercenter.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class VCommentService {
	private final VCommentRepository dao;
	
	public List<VComment> write(VCommentDto.Write dto, String profile, String loginId) {
		VComment vcomment = VComment.builder().vocBoard(VocBoard.builder().vbno(dto.getVbno()).build()).writer(loginId).content(dto.getContent()).profile(profile).build();
		dao.save(vcomment);
		
		// stream을 사용하지 않는 이유 : stream은 읽기 전용
		List<VComment> result = dao.findByVocBoardOrderByVcnoDesc(VocBoard.builder().vbno(dto.getVbno()).build());
		for(VComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
		
		/* stream을 사용한다면....링크없는 Comment를 링크걸린 Comment로 변환(map)
		List<Comment> resultC1 = dao.findByBoardOrderByCnoDesc(Board.builder().bno(dto.getBno()).build());
		return resultC1.stream().map(c1->new Comment(c1.getCno(), c1.getBoard(), c1.getWriter(), c1.getContent(), ZmallConstant.PROFILE_URL + c1.getProfile())).collect(Collectors.toList());
		*/
	}

	public List<VComment> delete(VCommentDto.Delete dto, String loginId) {
		VComment vcomment = dao.findById(dto.getVcno()).orElseThrow(BoardFail.CommentNotFoundException::new);
		if(vcomment.getWriter().equals(loginId)==false) 
			throw new BoardFail.IllegalJobException();
		dao.delete(vcomment);
		
		List<VComment> result = dao.findByVocBoardOrderByVcnoDesc(VocBoard.builder().vbno(dto.getVbno()).build());
		for(VComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
	}
}
