package com.demo.cdmall1.domain.customercenter.entity;

import java.util.*;

import org.springframework.data.repository.*;


public interface VAttachmentRepository extends CrudRepository<VAttachment, VAttachmentId>{


	List<VAttachment> findByVocBoard(VocBoard vocBoard);

	Optional<VAttachment> findByAno(Integer ano);

}
