package com.demo.cdmall1.web.dto;

import java.time.*;
import java.util.List;

import javax.validation.constraints.*;

import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.util.validation.annotation.*;

import lombok.*;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MemberDto { 
	@Data 
	@AllArgsConstructor
	public static class Join {
		@NotBlank(message="필수 입력입니다")
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message="아이디는 대문자와 숫자 5~10자입니다")
		@Username
		private String username;
		
		@NotBlank(message="필수 입력입니다")
		@Pattern(regexp="^[가-힣]{2,10}$", message="이름은 한글 2~10자입니다")
		@Irum
		private String irum;
		
		@NotBlank(message="필수 입력입니다")
		@Email
		private String email;
		
		@NotNull
		@Password
		private String password;
		
		//	생일추가시 사용
		/*
		 * @DateTimeFormat(pattern="yyyy-MM-dd") private LocalDate birthday;
		 */
		
		private MultipartFile sajin;

		/*	생일추가시 사용

		 * public Member toEntity() { return
		 * Member.builder().username(username).irum(irum).email(email).password(password
		 * ).birthday(birthday).build(); }
		 */
		
		public Member toEntity() {
			return Member.builder().username(username).irum(irum).email(email).password(password).build();
		}
	}

	@Data
	public class Update {
		private String irum;
		
		@Email
		private String email;
		
		private String tel;
		
		@Password
		private String password;
		
		@Password
		private String newPassword;
		
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private LocalDate birthday;
		
		private MultipartFile sajin;

		private List<String> address;

		
		public boolean passwordCheck() {
			if(this.password!=null && this.newPassword!=null) 
				return this.password.equals(this.newPassword);
			return false;
		}
	}
	
	@Data
	public class ResetPwd {
		@NotNull
		@Username
		private String username;
		
		@NotNull
		@Email
		private String email;
	}
	
	@Data
	public class ChangePwd {
		private String password;
		private String newPassword;
	}
	
	//	채팅기능시 권한으로 회원 찾기 위해 필요한 Dto 클래스
	@Data
	public class FindByAuthority{
		private String username;
		public Member toEntity() {
			return Member.builder().username(username).build();
		}
	}
	
	//관리자가 읽을 수 있는 멤버 정보
	@Data
	public class readUser{
		private String username;
		private String irum;
		private String email;
		private String tel;
	}
}
