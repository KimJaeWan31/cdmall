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

import com.demo.cdmall1.domain.usedboard.entity.UsedBoard;
import com.demo.cdmall1.domain.usedboard.entity.UsedBoardDto;
import com.demo.cdmall1.domain.usedboard.service.UsedBoardService;
import com.demo.cdmall1.util.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class UsedBoardController {
	private final UsedBoardService usedService;
	// 이미지 첨부파일 보기
	@GetMapping(path={"/usedBoard/image", "/ubtemp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename, HttpServletRequest req) throws IOException {
		
		// 호출한 주소에 따라 폴더명을 계산하자
		String command = req.getRequestURI().substring(1, req.getRequestURI().lastIndexOf("/"));
		File file = new File(ZmallConstant.TEMP_FOLDER + imagename);
		if(command.equals("usedBoard"))
			file = new File(ZmallConstant.NBIMAGE_FOLDER + imagename);
		
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
		@PostMapping(value="/usedBoard/image", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> ckImageUpload(MultipartFile upload) {
			return ResponseEntity.ok(usedService.ckImageUpload(upload));
		}
		
		
		@PreAuthorize("isAuthenticated()")
		@PostMapping(path="/usedBoard/new", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> write(@Valid UsedBoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
			System.out.println("################");
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			UsedBoard usedBoard = usedService.write(dto, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/usedBoard/read").queryParam("ubno", usedBoard.getUbno()).build().toUri();
			return ResponseEntity.created(uri).body(usedBoard);

		}
		
		
		
		
		@GetMapping(path="/usedBoard/{ubno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> read(@PathVariable Integer ubno, Principal principal) {
			String username = (principal==null)? null : principal.getName();
			return ResponseEntity.ok(usedService.read(ubno, username));
		}
		
		@GetMapping(path="/usedBoard/all", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno, String writer) {
			//System.out.println("1234");
			return ResponseEntity.ok(usedService.list(pageno, writer));
			
		}
		

		
		
		@PutMapping(path="/usedBoard/{ubno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> update(@Valid UsedBoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			return ResponseEntity.ok(usedService.update(dto, principal.getName()));
		}
		

		
		@PostMapping("/usedBoard/comments")
		public ResponseEntity<?> CommentCnt(@RequestParam Integer ubno) {
			Integer cnt = usedService.updateCommentCnt(ubno);
			return ResponseEntity.ok(cnt);
		}
		
		
		@GetMapping("/usedBoard/good_or_bad")
		public ResponseEntity<?> GoodOrBadCnt(@RequestParam Integer ubno, @RequestParam Integer state) {
			Integer cnt = usedService.goodOrBad(ubno, state);
			return ResponseEntity.ok(cnt);
		}
		
		@GetMapping(path="/usedBoard/warnlist", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> warnlist(@RequestParam(defaultValue="1") Integer pageno, Integer warnCnt) {
			System.out.println("sssssssssssssssssssssss");
			return ResponseEntity.ok(usedService.warnList(pageno, warnCnt));
		}
		
		@GetMapping("/usedBoard/warn")
		public ResponseEntity<?> WarnCnt(@RequestParam Integer ubno, @RequestParam Integer state){
			Integer cnt = usedService.warnCheck(ubno, state);
			return ResponseEntity.ok(cnt);
		}
		
		
		@DeleteMapping("/usedBoard/{ubno}")
		public ResponseEntity<?> delete(@PathVariable Integer ubno, Principal principal) {
			usedService.delete(ubno, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
			
			// 201일 때는 주소를 보내줘야 한다. ResponseEntity의 created메소드는 uri를 주면 Location이름으로 헤더에 추가해준다
			// 201이 아니면 백에서 수동으로 헤더에 Location을 추가해야 한다
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", uri.toString());
			
			// ResponseEntity에 header를 추가하려면 new 해야 한다
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
		}
		
		// 검색
		@PostMapping(path="/usedBoard/searchAll", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> search(@RequestParam(defaultValue = "1") Integer pageno, HttpSession session){
			String word = session.getAttribute("word").toString();
			URI uri = UriComponentsBuilder.newInstance().path("/usedBoard/search").queryParam("word", word).build().toUri();
			Map<String, Object> board = usedService.readSearchAll(pageno, word);
			return ResponseEntity.created(uri).body(board);
		}

}
