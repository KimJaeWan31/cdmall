package com.demo.cdmall1.domain.board.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface BoardCustomRepository {
	// 페이지 번호, 작성자, 카테고리로 리스트를 읽어옮
		List<BoardDto.List> readAll(Pageable pageable, String writer, String category);
		
		public Long countAll(String writer, String category);
		
		// 페이지 번호와 추천수로 리스트를 읽어옮
		List<BoardDto.BestList> readBestAll(Pageable pageable, Integer goodCnt);
		
		List<BoardDto.BestRank> readBestRank(Pageable pageable, Integer goodCnt);
		
		List<BoardDto.WarnList> readWarnAll(Pageable pageable, Integer warnCnt);
		
		public Long countByGoodCnt2();
		
		public Long countByGoodCnt();
		
		public Long countByBno();
		
		public Long countByWarnCnt();
		

		// 검색
		List<BoardDto.List> search(Pageable pageable, String word);
		
		public Long countSearch(String word);
		
		//추천게시판 검색
		List<BoardDto.BestList> searchBest(Pageable pageable, String word);
		
		public Long countSearchBest(String word);
}
