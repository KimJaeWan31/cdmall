package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.noticeboard.entity.*;
import com.demo.cdmall1.domain.noticeboard.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class NoticeBoardController {
	private final NoticeBoardService noticeService;
	// 이미지 첨부파일 보기
		@GetMapping(path={"/noticeBoard/image", "/temp/image"}, produces=MediaType.IMAGE_JPEG_VALUE)
		public ResponseEntity<?> showImage(@RequestParam String imagename) throws IOException {
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
		@PostMapping(value="/noticeBoard/image", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> ckImageUpload(MultipartFile upload) {
			return ResponseEntity.ok(noticeService.ckImageUpload(upload));
		}
		
		@PreAuthorize("isAuthenticated()")
		@PostMapping(path="/noticeBoard/new", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> write(@Valid NoticeBoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			NoticeBoard noticeBoard = noticeService.write(dto, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/noticeBoard/read").queryParam("nbno", noticeBoard.getNbno()).build().toUri();
			return ResponseEntity.created(uri).body(noticeBoard);
		}
		
		@GetMapping(path="/noticeBoard/{nbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> read(@PathVariable Integer nbno, Principal principal) {
			String username = (principal==null)? null : principal.getName();
			return ResponseEntity.ok(noticeService.read(nbno, username));
		}
		
		@GetMapping(path="/noticeBoard/all", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno) {
			return ResponseEntity.ok(noticeService.list(pageno));
		}
		
		@PutMapping(path="/noticeBoard/{nbno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> update(@Valid NoticeBoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
			System.out.println(dto);
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			return ResponseEntity.ok(noticeService.update(dto, principal.getName()));
		}
		
		@DeleteMapping("/noticeBoard/{nbno}")
		public ResponseEntity<?> delete(@PathVariable Integer nbno, Principal principal) {
			noticeService.delete(nbno, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
			
			// 201일 때는 주소를 보내줘야 한다. ResponseEntity의 created메소드는 uri를 주면 Location이름으로 헤더에 추가해준다
			// 201이 아니면 백에서 수동으로 헤더에 Location을 추가해야 한다
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", uri.toString());
			
			// ResponseEntity에 header를 추가하려면 new 해야 한다
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
		}
}
