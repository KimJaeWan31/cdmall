package com.demo.cdmall1.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.access.*;
import org.springframework.security.web.access.*;
import org.springframework.stereotype.*;

// 권한없음이 발생했을 때 /error/e403으로 유도할 AccessDenialHandler
@Component
public class CdmallAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		HttpSession session = request.getSession();
		session.setAttribute("msg", "잘못된 접근입니다");
		response.sendRedirect("/");
	}
}
