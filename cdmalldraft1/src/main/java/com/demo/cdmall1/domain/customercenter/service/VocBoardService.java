package com.demo.cdmall1.domain.customercenter.service;

import java.io.*;


import java.nio.file.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import org.jsoup.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.customercenter.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.VCommentDto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class VocBoardService {
private final VocBoardRepository dao; 
	
	// 추가
	public VocBoard write(VocBoardDto.Write dto, String loginId) {
		VocBoard vocBoard = dto.toEntity().setWriter(loginId);
		Jsoup.parseBodyFragment(dto.getContent()).getElementsByTag("img").forEach(img->{
			// http://localhost:8081/temp/image?imagename=aaa.jpg;
			String imageUrl = img.attr("src");
			String imageName = imageUrl.substring(imageUrl.lastIndexOf("=")+1);
			File tempImage = new File(ZmallConstant.TEMP_FOLDER, imageName);
			File image = new File(ZmallConstant.IMAGE_FOLDER, imageName);
			try {
				if(tempImage.exists()) {
					FileCopyUtils.copy(Files.readAllBytes(tempImage.toPath()), image);
					tempImage.delete();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		vocBoard.setContent(dto.getContent().replaceAll(ZmallConstant.CK_FIND_PATTERN, ZmallConstant.CK_REPLACE_PATTERN));
		
		if(dto.getVattachments()==null)
			return dao.save(vocBoard);
		
		dto.getVattachments().forEach(uploadFile->{
			// 저장할 파일 이름을 지정(현재시간을 1/1000초 단위로 계산)
			String saveFileName = System.currentTimeMillis() + "-" + uploadFile.getOriginalFilename();
			File saveFile = new File(ZmallConstant.ATTACHMENT_FOLDER, saveFileName);
			try {
				uploadFile.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			boolean isImage = uploadFile.getContentType().toLowerCase().startsWith("image/");
			// board는 관계의 주인이 아니다. board쪽에서 attachment를 저장하면 반영이 안된다
			vocBoard.addVAttachment(new VAttachment(0, null, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
		});
		return dao.save(vocBoard);
	}
	
	// 읽기
	@Transactional
	public Map<String,Object> read(Integer vbno, String loginId) {
		VocBoard vocBoard = dao.findById(vbno).orElseThrow(BoardFail.BoardNotFoundException::new);
		vocBoard.increaseReadCnt(loginId);		
		List<Read> vcomments = vocBoard.getVcomments().stream().map(c->c.toDto()).collect(Collectors.toList());
		Map<String,Object> map = new HashMap<>();
		map.put("vbno", vocBoard.getVbno());
		map.put("title", vocBoard.getTitle());
		map.put("content", vocBoard.getContent());
		map.put("commentCnt", vocBoard.getCommentCnt());
		// map에는 @JsonFormat을 걸수가 없으므로 직접 변환해서 map에 저장하자
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		map.put("createTime", dtf.format(vocBoard.getCreateTime()));
		map.put("readCnt", vocBoard.getReadCnt());
		map.put("updateTime", vocBoard.getUpdateTime());
		map.put("writer", vocBoard.getWriter());
		map.put("vattachments", vocBoard.getVattachments());
		map.put("vcomments", vcomments);
		return map;
	}
	
	// 글 변경
	@Transactional
	public VocBoard update(VocBoardDto.Update dto, String loginId) {
		VocBoard vocBoard = dao.findById(dto.getVbno()).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(vocBoard.getWriter().equals(loginId)==false)
			throw new BoardFail.IllegalJobException();
		return vocBoard.update(dto);
	}
	
	@Transactional
	public Integer updateVCommentCnt(Integer vbno) {
		VocBoard vocBoard = dao.findById(vbno).orElseThrow(BoardFail.IllegalJobException::new);
		return vocBoard.updateCommentCnt();
	}
	
	public Map<String,Object> list(Integer pageno) {
		// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
		Pageable pageable = PageRequest.of(pageno-1, 10);
		Map<String,Object> map = new HashMap<>();
		map.put("content", dao.readAll(pageable));
		map.put("totalcount", dao.countByVbno());
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}
	
	public CKResponse ckImageUpload(MultipartFile upload) {
		if(upload!=null && upload.isEmpty()==false && upload.getContentType().toLowerCase().startsWith("image/")) {
			String imageName = UUID.randomUUID().toString() + ".jpg";
			File file = new File(ZmallConstant.TEMP_FOLDER, imageName);
			try {
				upload.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			return new CKResponse(1, imageName, ZmallConstant.TEMP_URL + imageName);
		}
		return null;
	}

	@Transactional
	public Void delete(Integer vbno, String loginId) {
		VocBoard vocBoard = dao.findById(vbno).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(vocBoard.getWriter().equals(loginId)==false)
			throw new BoardFail.IllegalJobException();
		dao.delete(vocBoard);
		return null;
	}

}








