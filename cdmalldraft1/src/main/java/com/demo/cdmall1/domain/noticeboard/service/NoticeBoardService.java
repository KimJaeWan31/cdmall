package com.demo.cdmall1.domain.noticeboard.service;

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
import com.demo.cdmall1.domain.imageboard.entity.*;
import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class NoticeBoardService {
private final NoticeBoardRepository dao; 
	
	public NoticeBoard write(NoticeBoardDto.Write dto, String loginId) {
		NoticeBoard noticeBoard = dto.toEntity().setWriter(loginId);
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
		
		noticeBoard.setContent(dto.getContent().replaceAll(ZmallConstant.CK_FIND_PATTERN, ZmallConstant.CK_REPLACE_PATTERN));
		
		if(dto.getNbattachments()==null)
			return dao.save(noticeBoard);
		
		dto.getNbattachments().forEach(uploadFile->{
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
			noticeBoard.addAttachment(new NBAttachment(0, null, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
		});
			return dao.save(noticeBoard);
	}
	
	// 읽기
		@Transactional
		public Map<String,Object> read(Integer nbno, String loginId) {
			NoticeBoard noticeBoard = dao.findById(nbno).orElseThrow(BoardFail.BoardNotFoundException::new);
			noticeBoard.increaseReadCnt(loginId);	
			Map<String,Object> map = new HashMap<>();
			map.put("nbno", noticeBoard.getNbno());
			map.put("title", noticeBoard.getTitle());
			map.put("content", noticeBoard.getContent());
			// map에는 @JsonFormat을 걸수가 없으므로 직접 변환해서 map에 저장하자
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
			map.put("createTime", dtf.format(noticeBoard.getCreateTime()));
			map.put("readCnt", noticeBoard.getReadCnt());
			map.put("updateTime", noticeBoard.getUpdateTime());
			map.put("writer", noticeBoard.getWriter());
			map.put("nbattachments", noticeBoard.getNbattachments());
			return map;
		}
		
		public Map<String,Object> list(Integer pageno) { 
			Pageable pageable = PageRequest.of(pageno-1, 10); 
			Map<String,Object> map = new HashMap<>(); 
			map.put("content", dao.readAll(pageable));
			map.put("totalcount", dao.countByNbno()); 
			map.put("pageno", pageno);
			map.put("pagesize", 10);
			return map; 
		 }

		
	// ck 이미지 업로드
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
	
	// 글 변경
			@Transactional
			public NoticeBoard update(NoticeBoardDto.Update dto, String loginId) {
				NoticeBoard noticeBoard = dao.findById(dto.getNbno()).orElseThrow(BoardFail.BoardNotFoundException::new);
				if(noticeBoard.getWriter().equals(loginId)==false)
					throw new BoardFail.IllegalJobException();
				return noticeBoard.update(dto);
			}
			
	// 글 삭제
		public void delete(Integer nbno, String loginId) {
			NoticeBoard noticeBoard = dao.findById(nbno).orElseThrow(BoardFail.BoardNotFoundException::new);
			if(noticeBoard.getWriter().equals(loginId)==false)
				throw new BoardFail.IllegalJobException();
			dao.delete(noticeBoard);
		}
}
