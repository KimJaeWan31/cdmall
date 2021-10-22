package com.demo.cdmall1.domain.usedboard.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.*;

public interface UsedBoardCustomRepository {
	List<UsedBoardDto.ListView> readAll(Pageable pageable,String writer);
	public Long countAll(String writer);
	
	List<UsedBoardDto.WarnList> readWarnAll(Pageable pageable, Integer warnCnt);
	public Long countByWarnCnt();
}
