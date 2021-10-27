package com.demo.cdmall1.domain.customercenter.service;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.customercenter.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class VAttachmentService {
	private final VAttachmentRepository dao;

	@Transactional
	public List<VAttachment> delete(Integer vbno, Integer vano) {
		VAttachmentId id = new VAttachmentId(vbno, vano);
		VAttachment vattachment = dao.findById(id).orElseThrow(VocBoardFail.VAttachmentNotFoundException::new);
		dao.delete(vattachment);
		return dao.findByVocBoard(VocBoard.builder().vbno(vbno).build());
	}

	public VAttachment read(Integer vano) {
		return dao.findByVano(vano).orElseThrow(VocBoardFail.VAttachmentNotFoundException::new);
	}
}
