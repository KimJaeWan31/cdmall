package com.demo.cdmall1.domain.product.entity;

import java.util.*;

import org.springframework.data.repository.*;


public interface ReviewAttachmentRepository extends CrudRepository<ReviewAttachment, Integer>{


	List<ReviewAttachment> findByReview(Review Review);

	Optional<ReviewAttachment> findByRano(Integer ano);

}
