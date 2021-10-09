package com.demo.cdmall1.web.dto;

import java.time.*;

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
		@NotNull
		@Username
		private String username;
		
		@NotNull
		@Irum
		private String irum;
		
		@NotNull
		@Email
		private String email;
		
		@NotNull
		@Password
		private String password;
		
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private LocalDate birthday;
		
		private MultipartFile sajin;

		public Member toEntity() {
			return Member.builder().username(username).irum(irum).email(email).password(password).birthday(birthday).build();
		}
	}

	@Data
	public class Update {
		@NotNull
		@Email
		private String email;
		
		@Password
		private String password;
		
		@Password
		private String newPassword;
		
		private LocalDate birthday;
		
		private MultipartFile sajin;

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
		@NotNull
		@Password
		private String password;
		
		@NotNull
		@Password
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
}
