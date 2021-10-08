package com.demo.cdmall1.domain.noticeboard.entity;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface NBCommentRepository extends JpaRepository<NBComment, Integer>{
	List<NBComment> findByNoticeBoardOrderByNbcnoDesc(NoticeBoard build);
}
