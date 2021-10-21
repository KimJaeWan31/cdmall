package com.demo.cdmall1.domain.member.entity;

import java.io.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// 복합기본키를 사용하려면 복합키 클래스를 정의해야 한다
// 1. 복합키 클래스는 반드시 implements Serializable
//		Serialization(직렬화) : 객체를 저장하고 읽어오는 것
// 	  자바에서 직렬화하려면 implements Serializable 필요 -> 인터페이스도 어노테이션과 비슷한 역할
// 2. 복합키 클래스를 사용하는 클래스를 만들때 필드 이름을 그대로 사용해야 한다
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityId implements Serializable {
	private String member;
	private String authorityName;
}
