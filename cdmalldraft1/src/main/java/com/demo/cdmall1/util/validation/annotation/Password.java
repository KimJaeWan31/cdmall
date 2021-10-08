package com.demo.cdmall1.util.validation.annotation;

import java.lang.annotation.*;

import javax.validation.*;

import com.demo.cdmall1.util.validation.validator.*;

// 적용 위치 : 클래스, 필드, 메소드, 파라미터
@Target({ElementType.FIELD, ElementType.PARAMETER})
// 언제 동작할거니?
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
	// 검증 실패시 출력할 기본 메시지
	String message() default "비밀번호는 특수문자를 포함하는 영숫자와 특수문자 8~10자입니다";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {}; 
}
