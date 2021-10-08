package com.demo.cdmall1.domain.noticeboard.service;

import java.util.*;

import javax.validation.*;

import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class NBCommentService {
	private final NBCommentRepository dao;
	
	public List<NBComment> write(NBCommentDto.Write dto, String profile, String loginId) {
		NBComment nbcomment = NBComment.builder().noticeBoard(NoticeBoard.builder().nbno(dto.getNbno()).build()).writer(loginId).content(dto.getContent()).profile(profile).build();
		dao.save(nbcomment);
		
		// stream을 사용하지 않는 이유 : stream은 읽기 전용
		List<NBComment> result = dao.findByNoticeBoardOrderByNbcnoDesc(NoticeBoard.builder().nbno(dto.getNbno()).build());
		for(NBComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
		
		/* stream을 사용한다면....링크없는 Comment를 링크걸린 Comment로 변환(map)
		List<Comment> resultC1 = dao.findByBoardOrderByCnoDesc(Board.builder().bno(dto.getBno()).build());
		return resultC1.stream().map(c1->new Comment(c1.getCno(), c1.getBoard(), c1.getWriter(), c1.getContent(), ZmallConstant.PROFILE_URL + c1.getProfile())).collect(Collectors.toList());
		*/
	}

	public List<NBComment> delete(NBCommentDto.Delete dto, String loginId) {
		System.out.println(dto);
		NBComment nbcomment = dao.findById(dto.getNbcno()).orElseThrow(BoardFail.CommentNotFoundException::new);
		if(nbcomment.getWriter().equals(loginId)==false) 
			throw new BoardFail.IllegalJobException();
		dao.delete(nbcomment);
		
		List<NBComment> result = dao.findByNoticeBoardOrderByNbcnoDesc(NoticeBoard.builder().nbno(dto.getNbno()).build());
		for(NBComment c:result) 
			c.setProfile(ZmallConstant.PROFILE_URL + c.getProfile());
		return result;
	}
}
