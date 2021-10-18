package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;

import javax.servlet.http.*;
import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.board.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class BoardController {
private final BoardService service;
	
	// 이미지 첨부파일 보기
	@GetMapping(path={"/board/image", "/temp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename, HttpServletRequest req) throws IOException {
		File file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
		
		System.out.println(file);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(ZmallUtil.getMediaType(imagename));
		headers.add("Content-Disposition", "inline;filename="  + imagename);
		try {
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ck 이미지 업로드
	@PostMapping(value="/board/image", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ckImageUpload(MultipartFile upload) {
		return ResponseEntity.ok(service.ckImageUpload(upload));
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(path="/board/new", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> write(@Valid BoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		Board board = service.write(dto, principal.getName());
		URI uri = UriComponentsBuilder.newInstance().path("/board/read").queryParam("bno", board.getBno()).build().toUri();
		return ResponseEntity.created(uri).body(board);
	}
	
	@GetMapping(path="/board/{bno}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> read(@PathVariable Integer bno, Principal principal) {
		String username = (principal==null)? null : principal.getName();
		return ResponseEntity.ok(service.read(bno, username));
	}
	
	@GetMapping(path="/board/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno, String writer, String category) {
		return ResponseEntity.ok(service.list(pageno, writer, category));
	}
	
	@GetMapping(path="/board/warnlist", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> warnlist(@RequestParam(defaultValue="1") Integer pageno, Integer warnCnt) {
		System.out.println("sssssssssssssssssssssss");
		return ResponseEntity.ok(service.warnList(pageno, warnCnt));
	}
	
	@GetMapping(path="/board/best", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bestlist(@RequestParam(defaultValue = "1") Integer pageno, Integer goodCnt){
		return ResponseEntity.ok(service.bestList(pageno, goodCnt));
	}
	
	
	@PutMapping(path="/board/{bno}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@Valid BoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		return ResponseEntity.ok(service.update(dto, principal.getName()));
	}
	
	@DeleteMapping(path="/board/{bno}")
	public ResponseEntity<?> delete(@PathVariable Integer bno, Principal principal) throws BindException {
		return ResponseEntity.ok(service.delete(bno, principal.getName()));
	}
		
	@PostMapping("/board/comments")
	public ResponseEntity<?> CommentCnt(@RequestParam Integer bno) {
		Integer cnt = service.updateCommentCnt(bno);
		return ResponseEntity.ok(cnt);
	}
	
	@PostMapping("/board/inactive")
	public ResponseEntity<?> update_isActive(@RequestParam Integer bno) {
		Boolean isActive = service.updateIsActive(bno);
		return ResponseEntity.ok(isActive);
	}
	
	@GetMapping("/board/good_or_bad")
	public ResponseEntity<?> GoodOrBadCnt(@RequestParam Integer bno, @RequestParam Integer state) {
		Integer cnt = service.goodOrBad(bno, state);
		return ResponseEntity.ok(cnt);
	}
	
	@GetMapping("/board/warn")
	public ResponseEntity<?> WarnCnt(@RequestParam Integer bno, @RequestParam Integer state){
		Integer cnt = service.warnCheck(bno, state);
		return ResponseEntity.ok(cnt);
	}
	
	@PostMapping("/board/attachment")
	public ResponseEntity<?> updateAttachmentCnt(Integer bno) {
		Integer cnt = service.updateAttachment(bno);
		return ResponseEntity.ok(cnt);
	}
	
}





