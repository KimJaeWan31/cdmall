package com.demo.cdmall1.domain.member.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;

import org.hibernate.annotations.*;

import com.demo.cdmall1.domain.jpa.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;
import lombok.experimental.*;


// MVC에서는 예를 들어 날짜를 출력하려면 String 변환이 필요. DTO를 사용해서 원하는 출력 형태를 구성한다
// REST는 JSON 출력. JSON은 어차피 문자열이므로 MessageConverter 설정만 해주면 원하는 포맷 출력이 가능 -> JsonIgnore, JsonFormat
//		즉 REST 출력의 경우 보통은 DTO 안만들고 엔티티 + 어노테이션
// 		그런데 Member에 가입기간을 추가해서 출력하자


// 클래스의 필드를 초기화하는 방법 : instance 초기화 -> static 초기화 -> 생성자
// lombok의 빌더를 사용하면 instance 초기화 동작 안함
@Getter
@Setter
@ToString(exclude={"authorities","address"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
@Entity
@DynamicUpdate
public class Member extends BaseCreateTimeEntity {
	@PrePersist
	public void init() {
		point = 0;
		//buyCount = 0;
		//buyMoney = 0;
		loginFailCnt = 0;
		enabled = false;
		level = Level.BRONZE;
	}
	
	@Id
	@Column(length=10)
	private String username;
	
	@Column(length=10)
	private String irum;
	
	@Column(length=30)
	private String email;
	
	@JsonIgnore
	@Column(length=60)
	private String password;
	
	@Column(length=30)
	private String tel;
	
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	//@JsonFormat(pattern = "yyyy년 MM월 dd일")			사용자  -> 스프링 ProperyEditor			<- MessageConverter
	private LocalDate birthday;
	
	private String profile;
	
	private Integer point;
	
	/*
	 * private Integer buyCount;
	 * 
	 * private Integer buyMoney;
	 */
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="levels")
	private Level level;
	
	@JsonIgnore
	private Integer loginFailCnt;
	
	//@JsonIgnore
	private Boolean enabled;
	
	@JsonIgnore
	@Column(length=20)
	private String checkcode;
	
	@JsonIgnore
	@OneToMany(mappedBy="member", cascade={CascadeType.MERGE, CascadeType.REMOVE})
	private Set<Authority> authorities;
	
	@JsonIgnore
	@OneToMany(mappedBy="member", cascade={CascadeType.MERGE, CascadeType.REMOVE})
	@OrderBy(value="adno DESC")
	private Set<Address> address;
	
	@JsonIgnore
	@OneToMany(mappedBy="member", cascade={CascadeType.MERGE, CascadeType.REMOVE})
	@OrderBy(value="petno DESC")
	private Set<Pet> pets;
	
	// DB에 저장되지 않는 필드
	@Transient
	private Long days;

	public void addJoinInfo(String profile, String checkcode, String encodedPassword, List<String> authorities) {
		if(this.authorities==null)
			this.authorities = new HashSet<Authority>();
		this.profile = profile;
		this.checkcode = checkcode;
		this.password = encodedPassword;
		authorities.forEach(authorityName->this.authorities.add(new Authority(this, authorityName)));
	}
	
	//주소록 저장
		public void addAddressInfo(List<String> adrressList ) {
			if(this.address==null)
				this.address = new HashSet<Address>();
			adrressList.forEach(addressName->this.address.add(new Address(0, this, addressName)));
		}
		
		//펫 정보 저장
		public void setPets(String petName, String petSajin, Integer petAge, Boolean gender, String petKind) {
			if(this.pets==null)
				this.pets = new HashSet<Pet>();
			this.pets.add(new Pet(0, this, petName, petSajin, petAge, gender, petKind));
			
		}
	
	public void loginFail() {
		if(this.loginFailCnt<4)
			this.loginFailCnt++;
		else {
			this.loginFailCnt++;
			this.enabled = false;
		}
	}

	public void loginSuccess() {
		this.loginFailCnt = 0;
		this.enabled = true;
	}
	
}