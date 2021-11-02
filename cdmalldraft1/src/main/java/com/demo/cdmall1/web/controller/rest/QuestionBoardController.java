package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

import javax.servlet.http.*;
import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.questionboard.entity.*;
import com.demo.cdmall1.domain.questionboard.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class QuestionBoardController {
	private final QuestionBoardService questionService;
	// 이미지 첨부파일 보기
	@GetMapping(path={"/questionBoard/image", "/qbtemp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename, HttpServletRequest req) throws IOException {
		
		// 호출한 주소에 따라 폴더명을 계산하자
		String command = req.getRequestURI().substring(1, req.getRequestURI().lastIndexOf("/"));
		File file = new File(ZmallConstant.TEMP_FOLDER + imagename);
		if(command.equals("questionBoard"))
			file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
		
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
		@PostMapping(value="/questionBoard/image", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> ckImageUpload(MultipartFile upload) {
			return ResponseEntity.ok(questionService.ckImageUpload(upload));
		}
		
		
		@PreAuthorize("isAuthenticated()")
		@PostMapping(path="/questionBoard/new", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> write(@Valid QuestionBoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
			System.out.println("################");
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			QuestionBoard questionBoard = questionService.write(dto, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/questionBoard/read").queryParam("qbno", questionBoard.getQbno()).build().toUri();
			return ResponseEntity.created(uri).body(questionBoard);

		}
		
		
		
		
		@GetMapping(path="/questionBoard/{qbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> read(@PathVariable Integer qbno, Principal principal) {
			String username = (principal==null)? null : principal.getName();
			return ResponseEntity.ok(questionService.read(qbno, username));
		}
		
		@GetMapping(path="/questionBoard/all", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno, String writer) {
			//System.out.println("1234");
			return ResponseEntity.ok(questionService.list(pageno, writer));
			
		}
		

		
		
		@PutMapping(path="/questionBoard/{qbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> update(@Valid QuestionBoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			return ResponseEntity.ok(questionService.update(dto, principal.getName()));
		}
		

		
		@PostMapping("/questionBoard/comments")
		public ResponseEntity<?> CommentCnt(@RequestParam Integer qbno) {
			Integer cnt = questionService.updateCommentCnt(qbno);
			return ResponseEntity.ok(cnt);
		}
		
		
		@GetMapping("/questionBoard/good_or_bad")
		public ResponseEntity<?> GoodOrBadCnt(@RequestParam Integer qbno, @RequestParam Integer state) {
			Integer cnt = questionService.goodOrBad(qbno, state);
			return ResponseEntity.ok(cnt);
		}
		
		
		@DeleteMapping("/questionBoard/{qbno}")
		public ResponseEntity<?> delete(@PathVariable Integer qbno, Principal principal) {
			questionService.delete(qbno, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
			
			// 201일 때는 주소를 보내줘야 한다. ResponseEntity의 created메소드는 uri를 주면 Location이름으로 헤더에 추가해준다
			// 201이 아니면 백에서 수동으로 헤더에 Location을 추가해야 한다
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", uri.toString());
			
			// ResponseEntity에 header를 추가하려면 new 해야 한다
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
		}
		
		// 검색
		@PostMapping(path="/questionBoard/searchAll", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> search(@RequestParam(defaultValue = "1") Integer pageno, HttpSession session){
			String word = session.getAttribute("word").toString();
			URI uri = UriComponentsBuilder.newInstance().path("/questionBoard/search").queryParam("word", word).build().toUri();
			Map<String, Object> board = questionService.readSearchAll(pageno, word);
			return ResponseEntity.created(uri).body(board);
		}

}
