package com.demo.cdmall1.domain.board.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface BoardCustomRepository {
	List<BoardDto.ListView> readAll(Pageable pageable, String writer);
	
	public Long countAll(String writer);

}
