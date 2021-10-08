package com.demo.cdmall1.domain.noticeboard.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.NoticeBoardDto;

public interface NoticeBoardCustomRepository {
	List<NoticeBoardDto.List> readAll(Pageable pageable);
	
	public Long countByNbno();
}
