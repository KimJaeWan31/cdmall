package com.demo.cdmall1.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.*;

@Component
public class AdminLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// RequestCache : 사용자가 가려던 목적지를 저장하는 인터페이스
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);		
		RedirectStrategy rs = new DefaultRedirectStrategy();
		if(savedRequest!=null)
			rs.sendRedirect(request, response, savedRequest.getRedirectUrl());
		else
			rs.sendRedirect(request, response, "/admin/index");
	}
}