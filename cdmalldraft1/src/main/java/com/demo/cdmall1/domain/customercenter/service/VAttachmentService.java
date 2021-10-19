package com.demo.cdmall1.domain.customercenter.service;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.board.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class VAttachmentService {
	private final AttachmentRepository dao;

	@Transactional
	public List<Attachment> delete(Integer bno, Integer ano) {
		AttachmentId id = new AttachmentId(bno, ano);
		Attachment attachment = dao.findById(id).orElseThrow(BoardFail.AttachmentNotFoundException::new);
		dao.delete(attachment);
		return dao.findByBoard(Board.builder().bno(bno).build());
	}

	public Attachment read(Integer ano) {
		return dao.findByAno(ano).orElseThrow(BoardFail.AttachmentNotFoundException::new);
	}
}
