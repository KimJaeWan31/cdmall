package com.demo.cdmall1.util.validation.validator;

import javax.validation.*;

import com.demo.cdmall1.util.validation.annotation.*;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	private final static String pattern = "^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$";
	
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if(password==null)
			return true;
		return password.matches(pattern);
	}

}
