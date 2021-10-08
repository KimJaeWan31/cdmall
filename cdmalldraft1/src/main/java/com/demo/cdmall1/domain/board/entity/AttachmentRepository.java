package com.demo.cdmall1.domain.board.entity;

import java.util.*;

import org.springframework.data.repository.*;


public interface AttachmentRepository extends CrudRepository<Attachment, AttachmentId>{


	List<Attachment> findByBoard(Board board);

	Optional<Attachment> findByAno(Integer ano);

}
