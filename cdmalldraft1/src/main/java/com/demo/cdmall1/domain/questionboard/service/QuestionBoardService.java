package com.demo.cdmall1.domain.questionboard.service;

import java.io.*;

import java.nio.file.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import org.jsoup.*;
import org.modelmapper.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service	
public class QuestionBoardService {
private final QuestionBoardRepository dao; 
private final ModelMapper modelMapper;


	public QuestionBoard write(QuestionBoardDto.Write dto, String loginId) {
		QuestionBoard questionBoard = dto.toEntity().setWriter(loginId);
		System.out.println(dto);
		Jsoup.parseBodyFragment(dto.getContent()).getElementsByTag("img").forEach(img->{
			String imageUrl = img.attr("src");
			String imageName = imageUrl.substring(imageUrl.lastIndexOf("=")+1);
			File tempImage = new File(ZmallConstant.TEMP_FOLDER, imageName);
			File image = new File(ZmallConstant.IMAGE_FOLDER, imageName);
			System.out.println("xxxxxxxxxxxxxxxx");
			try {
				if(tempImage.exists()) {
					FileCopyUtils.copy(Files.readAllBytes(tempImage.toPath()), image);
					tempImage.delete();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		
	questionBoard.setContent(dto.getContent().replaceAll(ZmallConstant.CK_FIND_PATTERN, ZmallConstant.CK_REPLACE_PATTERN));
	
	if(dto.getQsattachments()==null)
		return dao.save(questionBoard);
	
	dto.getQsattachments().forEach(uploadFile->{
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
		questionBoard.addQSAttachment (new QSAttachment(0, null, uploadFile.getOriginalFilename(), saveFileName, uploadFile.getSize(), isImage));
	});
	return dao.save(questionBoard);
	}
	
	// 읽기
//		@Transactional
//		public Map<String,Object> read(Integer qbno, String loginId) {
//			QuestionBoard questionBoard = dao.findById(nbno).orElseThrow(BoardFail.BoardNotFoundException::new);
//			questionBoard.increaseReadCnt(loginId);		
//			List<QSCommentDto.Read> comments = questionBoard.getComments().stream().map(c->c.toDto()).collect(Collectors.toList());
//			Map<String,Object> map = new HashMap<>();
//			map.put("nbno", questionBoard.getQbno());
//			map.put("title", questionBoard.getTitle());
//			map.put("content", questionBoard.getContent());
//			map.put("commentCnt", questionBoard.getCommentCnt());
//				
//			// map에는 @JsonFormat을 걸수가 없으므로 직접 변환해서 map에 저장하자
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//			map.put("createTime", dtf.format(questionBoard.getCreateTime()));
//			map.put("readCnt", questionBoard.getReadCnt());
//			map.put("updateTime", questionBoard.getUpdateTime());
//			map.put("writer", questionBoard.getWriter());
//			map.put("attachments", questionBoard.getQSAttachments());
//			map.put("comments", comments);
//			return map;
//		}
//		
	@Transactional
	public QuestionBoardDto.Read read(Integer qbno, String loginId) {
		QuestionBoard questionBoard = dao.findById(qbno).orElseThrow(BoardFail.BoardNotFoundException::new);
		questionBoard.increaseReadCnt(loginId);		
		List<QSCommentDto.Read> qscomments = questionBoard.getComments().stream().map(c->c.toDto()).collect(Collectors.toList());
		QuestionBoardDto.Read dto = modelMapper.map(questionBoard, QuestionBoardDto.Read.class);
		return dto.setQscomments (qscomments);
	}
	
		// 글 변경
		@Transactional
		public QuestionBoard update(QuestionBoardDto.Update dto, String loginId) {
			QuestionBoard questionBoard = dao.findById(dto.getQbno()).orElseThrow(BoardFail.BoardNotFoundException::new);
			if(questionBoard.getWriter().equals(loginId)==false)
				throw new BoardFail.IllegalJobException();
			return questionBoard.update(dto);
		}

		@Transactional
		public Integer updateCommentCnt(Integer nbno) {
			QuestionBoard questionBoard = dao.findById(nbno).orElseThrow(BoardFail.IllegalJobException::new);
			return questionBoard.updateCommentCnt();
		}

		public QuestionBoardDto.ListResponse list(Integer pageno, String writer) {
			// JPARepository의 findAll은 findById와 마찬가지로 관련 엔티티를 모두 읽어온다 -> 상관없다면 사용
			// Pageable pageable = PageRequest.of(pageno-1, 10, Sort.by(Sort.Direction.DESC, "bno"));
			// return dao.findAll(pageable);
			
			// 글의 전체 개수, 페이지 번호, 페이지 사이즈, content(글 목록)을 보내줘야 프론트에서 페이징할 수 있다....Map을 사용하자
			Pageable pageable = PageRequest.of(pageno-1, 10);
			QuestionBoardDto.ListResponse dto = new QuestionBoardDto.ListResponse(dao.readAll(pageable, writer), dao.countAll(writer), pageno, 10);
			//Map<String,Object> map = new HashMap<>();
			//map.put("content", dao.readAll(pageable, writer));
			//map.put("totalcount", dao.countAll(writer));
			//map.put("pageno", pageno);
			//map.put("pagesize", 10);

			return dto;
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
	public Integer goodOrBad(Integer qbno, Integer state) {
		QuestionBoard questionBoard = dao.findById(qbno).orElseThrow(BoardFail.IllegalJobException::new);
		if(state==0) {
			questionBoard.setGoodCnt(questionBoard.getGoodCnt()+1);
			return questionBoard.getGoodCnt();
		} else if(state==1) {
			questionBoard.setBadCnt(questionBoard.getBadCnt()+1);
			return questionBoard.getBadCnt();
		} else if(state==2)
			return questionBoard.getGoodCnt();
		return questionBoard.getBadCnt();
	}

	public void delete(Integer qbno, String loginId) {
		QuestionBoard questionBoard = dao.findById(qbno).orElseThrow(BoardFail.BoardNotFoundException::new);
		if(questionBoard.getWriter().equals(loginId)==false)
			throw new BoardFail.IllegalJobException();
		dao.delete(questionBoard);
	}
	


}
