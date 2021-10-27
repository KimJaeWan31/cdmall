package com.demo.cdmall1.domain.customercenter.entity;

import java.util.*;

import org.springframework.data.repository.*;

import com.demo.cdmall1.domain.board.entity.*;


public interface VAttachmentRepository extends CrudRepository<VAttachment, VAttachmentId>{

	List<VAttachment> findByVocBoard(VocBoard vocBoard);

	Optional<VAttachment> findByVano(Integer vano);

}
