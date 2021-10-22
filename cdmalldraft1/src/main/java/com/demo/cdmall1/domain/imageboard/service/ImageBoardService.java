package com.demo.cdmall1.domain.imageboard.service;

import java.io.File;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.cdmall1.domain.board.entity.BoardFail;
import com.demo.cdmall1.domain.imageboard.entity.*;
import com.demo.cdmall1.util.ZmallConstant;
import com.demo.cdmall1.web.dto.IBCommentDto;
import com.demo.cdmall1.web.dto.ImageBoardDto;

import lombok.*;

@RequiredArgsConstructor
@Service
public class ImageBoardService {
	private final ImageBoardRepository dao; 
	
	public ImageBoard write(ImageBoardDto.Write dto, String loginId) {
		ImageBoard imageBoard = dto.toEntity().setWriter(loginId);
		
		if(dto.getIbattachments()==null) {
			return dao.save(imageBoard);
		}
		
		dto.getIbattachments().forEach(uploadFile->{
			//imageBoard.setImageFileName(uploadFile.getOriginalFilename());
			// 저장할 파일 이름을 지정(현재시간을 1/1000초 단위로 계산)
			String saveFileName = System.currentTimeMillis() + "-" + uploadFile.getOriginalFilename();
			File saveFile = new File(ZmallConstant.IBIMAGE_FOLDER, saveFileName);
			imageBoard.setImageFileName(saveFileName);
			try {
				uploadFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			boolean isImage = uploadFile.getContentType().toLowerCase().startsWith("image/");
			// board는 관계의 주인이 아니다. board쪽에서 attachment를 저장하면 반영이 안된다
			imageBoard.addAttachment(new IBAttachment(0, null, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
		});
		
		return dao.save(imageBoard);
	}
	
	// 읽기
	@Transactional
	public Map<String,Object> read(Integer ibno, String loginId) {
		ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.BoardNotFoundException::new);		
		List<IBCommentDto.Read> ibcomments = imageBoard.getIbcomments().stream().map(c->c.toDto()).collect(Collectors.toList());
		Map<String,Object> map = new HashMap<>();
		map.put("ibno", imageBoard.getIbno());
		map.put("title", imageBoard.getTitle());
		map.put("content", imageBoard.getContent());
		map.put("goodCnt", imageBoard.getGoodCnt());
		map.put("reportCnt", imageBoard.getReportCnt());
		//map.put("badCnt", imageBoard.getBadCnt());
		// map에는 @JsonFormat을 걸수가 없으므로 직접 변환해서 map에 저장하자
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		map.put("createTime", dtf.format(imageBoard.getCreateTime()));
		map.put("updateTime", imageBoard.getUpdateTime());
		map.put("writer", imageBoard.getWriter());
		map.put("ibattachments", imageBoard.getIbattachments());
		map.put("ibcomments", ibcomments);
		map.put("imageFileName", imageBoard.getImageFileName());
		return map;
	}
		
	@Transactional
	public Integer updateIBCommentCnt(Integer ibno) {
		ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.IllegalJobException::new);
		return imageBoard.updateIBCommentCnt();
	}
	
	// 글 변경
	@Transactional
	public ImageBoard update(ImageBoardDto.Update dto, String loginId) {
		ImageBoard imageBoard = dao.findById(dto.getIbno()).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(imageBoard.getWriter().equals(loginId)==false)
			throw new BoardFail.IllegalJobException();
		return imageBoard.update(dto);
	}

	
	public Map<String,Object> list(Integer pageno) { 
		Pageable pageable = PageRequest.of(pageno-1, 9); 
		Map<String,Object> map = new HashMap<>(); 
		map.put("content", dao.readAll(pageable));
		System.out.println(map.get("content"));
		map.put("totalcount", dao.countByIbno()); 
		map.put("pageno", pageno);
		map.put("pagesize", 9);
		return map; 
	 }
	
	public Map<String,Object> reportList(Integer pageno, Integer reportCnt){
		Pageable pageable = PageRequest.of(pageno-1, 10);
		Map<String,Object> map = new HashMap<>();
		map.put("content", dao.readReportAll(pageable, reportCnt));
		map.put("totalcount", dao.countByReportCnt());
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}
	
	@Transactional
	public Integer goodOrBad(Integer ibno, Integer state) {
		ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(state==0) {
			imageBoard.setGoodCnt(imageBoard.getGoodCnt()+1);
			return imageBoard.getGoodCnt();
		} else if(state==2) {
			imageBoard.setGoodCnt(imageBoard.getGoodCnt()-1);
		}
		
		return imageBoard.getGoodCnt();
	}
	
	@Transactional
	public Integer isreportCheck(Integer ibno, Integer state) {
		ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(state==0) {
			imageBoard.setReportCnt(imageBoard.getReportCnt()+1);
			return imageBoard.getReportCnt();
		}else if(state==1) {
			return imageBoard.getReportCnt();
		}
		imageBoard.setReportCnt(imageBoard.getReportCnt()-1);
		return imageBoard.getReportCnt();
	}
	
	@Transactional
	public Integer isExistCheck(Integer ibno, Integer state) {
		ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(state==0) {
			imageBoard.setCheckCnt(imageBoard.getCheckCnt()+1);
			return imageBoard.getCheckCnt();
		}else if(state==1) {
			return imageBoard.getCheckCnt();
		}
		return state;
	}
	
	// 글 삭제
		public void delete(Integer ibno, String loginId) {
			ImageBoard imageBoard = dao.findById(ibno).orElseThrow(BoardFail.BoardNotFoundException::new);
			if(imageBoard.getWriter().equals(loginId)==false)
				throw new BoardFail.IllegalJobException();
			dao.delete(imageBoard);
		}
}
