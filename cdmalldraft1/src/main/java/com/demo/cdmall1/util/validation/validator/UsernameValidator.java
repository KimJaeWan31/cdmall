package com.demo.cdmall1.util.validation.validator;

import javax.validation.*;

import org.springframework.web.bind.*;

import com.demo.cdmall1.util.validation.annotation.*;

public class UsernameValidator implements ConstraintValidator<Username, String> {
	private final static String pattern = "^[A-Z0-9]{8,10}$";
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		// username이 필수입력인 경우 입력하지 않으면 MissingServletRequestParameterException -> @Username으로 체크하지 전에 예외처리
		// username이 선택입력인 경우 입력하지 않은 경우 패턴을 통과하지 못했다는 오류 발생 -> 선택입력이라면 패턴 체크를 수행하면 안된다
		System.out.println("=============================");
		System.out.println(username);
		System.out.println("=============================");
		if(username==null)
			return true;
		return username.matches(pattern);
	}

}
