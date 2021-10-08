package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.board.service.*;
import com.demo.cdmall1.util.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class AttachmentController {
	private final AttachmentService service;
	private final RestTemplate template;
	
	// ano로 첨부파일 삭제
	@DeleteMapping("/attachments")
	public ResponseEntity<?> delete(Integer bno, Integer ano) {
		// 첨부파일 삭제 -> bno로 첨부파일 목록을 보내자
		List<Attachment> attachments = service.delete(bno, ano);
				
		// BoardController에게 첨부파일 개수를 줄이라고 요청
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("bno", bno+"");
		Integer attachmentCnt = template.postForObject("http://localhost:8081/board/attachment", map, Integer.class);
		
		return ResponseEntity.ok(attachments);
	}
	
	@GetMapping(path="/attachments/{ano}", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> viewAttachment(@PathVariable Integer ano) {
		Attachment attachment = service.read(ano);
		// 사용자에게는 원래 파일이름으로
		String originalFileName = attachment.getOriginalFileName();
		File file = new File(ZmallConstant.ATTACHMENT_FOLDER, attachment.getSaveFileName());
		
		// header : inline 또는 attachment
		HttpHeaders headers = new HttpHeaders();
		if(attachment.getIsImage()==true) {
			headers.setContentType(ZmallUtil.getMediaType(originalFileName));
			headers.add("Content-Disposition", "inline;filename=" + originalFileName);
		} else
			headers.add("Content-Disposition", "attachment;filename=" + originalFileName);
		
		try {
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}








