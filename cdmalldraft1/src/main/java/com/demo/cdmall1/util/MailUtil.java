package com.demo.cdmall1.util;

import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import com.demo.cdmall1.web.dto.*;

@Component
public class MailUtil {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendJoinCheckMail(String from, String to, String checkCode) {
		Mail mail = Mail.builder().from(from).to(to).subject("가입 확인 메일").build();
		StringBuffer buf = new StringBuffer("<p>회원가입을 위한 안내 메일입니다</p>");
		buf.append("<p>가입 확인을 위해 아래 링크를 클릭하세요</p>");
		buf.append("<p>가입 확인 링크 :");
		buf.append("<a href='http://localhost:8081/members/join/check?checkcode=");
		buf.append(checkCode);
		buf.append("'>클릭하세요</a></p>");
		mail.setText(buf.toString());
		sendMail(mail);
	}
	
	public void sendSellerMail(String from, String to) {
		Mail mail = Mail.builder().from(from).to(to).subject("캣독왓숑 판매자 계정 신청 결과 안내").build();
		StringBuffer buf = new StringBuffer("<p>캣독왓숑 판매자 계정 신청이 거부됬습니다.</p>");
		buf.append("<p>자세한 신청 거부 사유가 궁금하시다면 </p>");
		buf.append("<p>캣독왓숑 홈페이지 고객센터의 1:1문의나 고객의 소리에서 문의해주세요</p>");
		buf.append("<a href='http://localhost:8081'>캣독왓숑 바로가기</a>");
		mail.setText(buf.toString());
		sendMail(mail);
	}
	
	private void sendMail(Mail mail) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, false, "utf-8");
			helper.setFrom(mail.getFrom());
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}	
	}

	public void sendResetPasswordMail(String from, String to, String password) {
		Mail mail = Mail.builder().from(from).to(to).subject("임시비밀번호").build();
		StringBuffer buf = new StringBuffer("<p>임시비밀번호를 발급했습니다</p>");
		buf.append("<p>임시비밀번호 :");
		buf.append(password);
		buf.append("</p>");
		mail.setText(buf.toString());
		sendMail(mail);
		
	}
}