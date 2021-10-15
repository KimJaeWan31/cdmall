package com.demo.cdmall1.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

@Component
public class AdminLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		session.setAttribute("msg", "로그인에 실패했습니다");
		new DefaultRedirectStrategy().sendRedirect(request, response, "/admin/login?error");
	}
}





