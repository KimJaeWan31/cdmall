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

import com.demo.cdmall1.domain.customercenter.entity.*;
import com.demo.cdmall1.domain.customercenter.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class VocBoardController {
	private final VocBoardService vocService;
	// 이미지 첨부파일 보기
	@GetMapping(path={"/vocBoard/image", "/temp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> showImage(@RequestParam String imagename, HttpServletRequest req) throws IOException {
			File file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
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
		@PostMapping(value="/vocBoard/image", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> ckImageUpload(MultipartFile upload) {
			return ResponseEntity.ok(vocService.ckImageUpload(upload));
		}
		
		
		@PreAuthorize("isAuthenticated()")
		@PostMapping(path="/vocBoard/new", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> write(@Valid VocBoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			VocBoard vocBoard = vocService.write(dto, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/customerCenter/vocRead").queryParam("vbno", vocBoard.getVbno()).build().toUri();
			return ResponseEntity.created(uri).body(vocBoard);

		}
		
		@GetMapping(path="/vocBoard/{vbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> read(@PathVariable Integer vbno, Principal principal) {
			String username = (principal==null)? null : principal.getName();
			return ResponseEntity.ok(vocService.read(vbno, username));
		}
		
		@GetMapping(path="/vocBoard/all", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno) {
			return ResponseEntity.ok(vocService.list(pageno));
			
		}
		
		@PutMapping(path="/vocBoard/{vbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> update(@Valid VocBoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			return ResponseEntity.ok(vocService.update(dto, principal.getName()));
		}
		
		@PostMapping("/vocBoard/vcomments")
		public ResponseEntity<?> VCommentCnt(@RequestParam Integer vbno) {
			Integer cnt = vocService.updateVCommentCnt(vbno);
			return ResponseEntity.ok(cnt);
		}                       
		
		@DeleteMapping("/vocBoard/{vbno}")
		public ResponseEntity<?> delete(@PathVariable Integer vbno, Principal principal) {
			vocService.delete(vbno, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
			
			// 201일 때는 주소를 보내줘야 한다. ResponseEntity의 created메소드는 uri를 주면 Location이름으로 헤더에 추가해준다
			// 201이 아니면 백에서 수동으로 헤더에 Location을 추가해야 한다
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", uri.toString());
			
			// ResponseEntity에 header를 추가하려면 new 해야 한다
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
		}
	
}
