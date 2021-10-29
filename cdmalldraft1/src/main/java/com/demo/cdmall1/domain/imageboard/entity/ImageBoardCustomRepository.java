package com.demo.cdmall1.domain.imageboard.entity;

import java.util.*;

import org.springframework.data.domain.*;

import com.demo.cdmall1.web.dto.BoardDto.WarnList;
import com.demo.cdmall1.web.dto.ImageBoardDto;
import com.demo.cdmall1.web.dto.ImageBoardDto.ReportList;

public interface ImageBoardCustomRepository {
	List<ImageBoardDto.List> readAll(Pageable pageable);
	
	public Long countByIbno();
	
	List<ImageBoardDto.ReportList> readReportAll(Pageable pageable, Integer reportCnt);
	
	public Long countByReportCnt();
	/*

	Long countByWarnCnt();

	List<ReportList> readWarnAll(Pageable pageable, Integer reportCnt);
*/

}
