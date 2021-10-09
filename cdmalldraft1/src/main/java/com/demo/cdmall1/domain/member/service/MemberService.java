package com.demo.cdmall1.domain.member.service;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.MemberDto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository dao;
	private final AuthorityRepository AuthDao;

	private final MailUtil mailUtil;
	private final PasswordEncoder passwordEncoder;
	
	// 매주 목요일 새벽 4시에 체크코드를 가지고 있는 멤버(가입신청만 하고 확인을 안했다)를 삭제
	// cron은 리눅스의 스케줄링 표현법이고 7자리(초 시 분 일 월 요일 년)인데 스프링만 년도를 제외한 6자리를 지
	@Scheduled(cron = "0 0 4 ? * THU")
	public void deleteMemberWithInvalidCheckcode() {
		List<Member> members = dao.findByCheckcodeIsNotNull();
		dao.deleteAll(members);
	}

	@Transactional(readOnly=true)
	public void idAvailabelCheck(String username) {
		if(dao.existsByUsername(username)==true)
			throw new MemberFail.UsernameExistException();
	}

	@Transactional(readOnly=true)
	public void emailAvailabelCheck(String email) {
		if(dao.existsByEmail(email)==true)
			throw new MemberFail.EmailExistException();
	}

	public void join(MemberDto.Join dto) {
		Member member = dto.toEntity();
		MultipartFile sajin = dto.getSajin();
		
		String profile = ZmallUtil.saveProfile(sajin, member.getUsername());
		String checkcode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		
		// member에 추가해야 할 필드 : profile, checkcode, 비밀번호, Set<Authority>
		member.addJoinInfo(profile, checkcode, encodedPassword, Arrays.asList("ROLE_USER"));
		dao.save(member);
		mailUtil.sendJoinCheckMail("admin@zmall.com", member.getEmail(), checkcode);
	}

	@Transactional
	public void joinCheck(String checkcode) {
		Member member = dao.findByCheckcode(checkcode).orElseThrow(MemberFail.JoinCheckFailException::new);
		member.setCheckcode(null).setEnabled(true);
	}

	@Transactional(readOnly=true)
	public String findId(String email) {
		return dao.findUsernameByEmail(email).orElseThrow(MemberFail.MemberNotFoundException::new);
	}

	@Transactional
	public void resetPassword(ResetPwd dto) {
		Member member = dao.findById(dto.getUsername()).orElseThrow(MemberFail.MemberNotFoundException::new);
		if(member.getEmail().equals(dto.getEmail())==false)
			throw new MemberFail.MemberNotFoundException();
		String newPassword = RandomStringUtils.randomAlphanumeric(20);
		member.setPassword(passwordEncoder.encode(newPassword));
		mailUtil.sendResetPasswordMail("admin@zmall.com", member.getEmail(), newPassword);
	}

	// 기존 비밀번호가 확인되면 새 비밀번호를 암호화해서 저장
	@Transactional
	public void changePassword(ChangePwd dto, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		if(dto.getPassword()!=null && dto.getNewPassword()!=null) {
			if(passwordEncoder.matches(dto.getPassword(), member.getPassword())==false)
				throw new MemberFail.PasswordCheckException();
			member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		}
	}
	
	@Transactional(readOnly=true)
	public void checkPassword(String password, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		if(passwordEncoder.matches(password, member.getPassword())==false)
			throw new MemberFail.PasswordCheckException();
	}

	// Member 처리 : 프사에 주소를 추가, transient 필드인 days에 값을 추가해서 내보내자
	public Member read(String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);	
		member.setProfile(ZmallConstant.PROFILE_URL + member.getProfile());
		
		// 작성일이 LocalDateTime -> LocalDate로 변환
		member.setDays(ChronoUnit.DAYS.between(LocalDate.from(member.getCreateTime()), LocalDate.now()));
		return member;
	}

	@Transactional
	public void update(MemberDto.Update dto, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		
		if(dto.getEmail()!=null)
			member.setEmail(dto.getEmail());
		
		if(dto.getPassword()!=null && dto.getNewPassword()!=null) {
			if(passwordEncoder.matches(dto.getPassword(), member.getPassword())==false)
				throw new MemberFail.PasswordCheckException();
			member.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		
		MultipartFile sajin = dto.getSajin();
		if(sajin!=null && sajin.isEmpty()==false) {
			String profile = ZmallUtil.saveProfile(sajin, member.getUsername());
			member.setProfile(profile);
		}
	}

	public void resign(String loginId) {
		dao.deleteById(loginId);
	}

	public String getProfile(String username) {
		return dao.findById(username).get().getProfile();
	}
	
	public Authority readAuthority(Member member) {
		Authority authority = AuthDao.findByMember(member);
		return authority;
	}
}
