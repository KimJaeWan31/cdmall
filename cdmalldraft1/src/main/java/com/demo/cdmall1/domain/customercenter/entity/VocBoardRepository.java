package com.demo.cdmall1.domain.customercenter.entity;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.*;

public interface VocBoardRepository extends JpaRepository<VocBoard, Integer>, VocBoardCustomRepository {

	//@transactional service에 걸어주기
		@Modifying
		@Query(value = "insert into VocBoard (vbno, title, content, writer, read_cnt, re_ref, re_lev, re_seq, create_time, update_time) VALUES (null, 'dsd', 'dddd', 'SPRING11', 0, 1, 0+1, 0+1, current_date, current_date)", nativeQuery = true)
		public void insertParentBoard();

		//@Modifying
		@Query("select max(re_ref) from VocBoard")
		public Integer selectMaxRef();

		@Transactional
		@Modifying
		@Query("update VocBoard set re_seq = re_seq + 1  where re_ref = ?1 and re_seq > ?2")
		public void updateReSeq(Integer re_ref, Integer re_seq);
		
		@Query("select vb from VocBoard vb order by re_ref desc, re_seq asc")
		public void updateList(Integer re_ref, Integer re_seq);
}
