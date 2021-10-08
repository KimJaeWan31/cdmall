package com.example.zmall2;

import java.util.*;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.demo.cdmall1.domain.board.entity.*;
import com.demo.cdmall1.domain.memo.entity.*;
import com.demo.cdmall1.memo.service.*;

@SpringBootTest
public class ZmallTest {
	@Autowired
	BoardRepository dao;
	@Autowired
	MemoService service;
	
	//@Test
	public void countTest() {
		dao.countAll(null);
		dao.countAll("spring11");
	}
	
	//@Test
	/*
	public void memoWriteTest() {
		for(int i=0; i<10; i++) {
			Memo memo = Memo.builder().title("연습").content("연습용").receiver("summer11").build();
			service.write(memo, "spring11");
		}
		for(int i=0; i<10; i++) {
			Memo memo = Memo.builder().title("연습").content("연습용").receiver("spring11").build();
			service.write(memo, "summer11");
		}
	}
	*/
	
	//@Test
	public void receiveList1Test() {
		service.receiveList1("spring11").forEach(m->System.out.println(m));
	}
	
	//@Test
	public void receiveListTest() {
		service.receiveList("spring11").forEach(m->System.out.println(m));
	}
	
	//@Test
	public void readTest() {
		service.read(24);
	}
	
	//@Test
	public void updateTest() {
		List<Integer> mnos = new ArrayList<>();
		mnos.add(21);
		mnos.add(22);
		service.disabledBySender(mnos);
	}
	
	//@Test
	public void deleteTest() {
		service.delete();
	}
	
	//@Test
	public void existTest() {
		//System.out.println(service.exists("WINTER"));
		//System.out.println(service.exists("SPRING11"));
	}
}







