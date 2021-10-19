package com.demo.cdmall1.domain.customercenter.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface VCommentRepository extends JpaRepository<VComment, Integer>{

	List<VComment> findByVocBoardOrderByVcnoDesc(VocBoard build);

}
