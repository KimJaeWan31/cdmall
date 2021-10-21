package com.demo.cdmall1.domain.member.service;

import java.time.*;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.demo.cdmall1.domain.board.entity.BoardRepository;
import com.demo.cdmall1.domain.imageboard.entity.ImageBoardRepository;
import com.demo.cdmall1.domain.member.entity.*;
import com.demo.cdmall1.domain.questionboard.entity.QuestionBoardRepository;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;
import com.demo.cdmall1.web.dto.AddressDto.Read;
import com.demo.cdmall1.web.dto.MemberDto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository dao;
	private final MemberDslRepository dslDao;
	private final AuthorityRepository AuthDao;
	private final BoardRepository bDao;
	private final ImageBoardRepository iDao;
	private final QuestionBoardRepository qDao;
	private final SellerRepository sDao;

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
	
	//업데이트 하기
	@Transactional
	public void update(MemberDto.Update dto, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		
		if(dto.getIrum()!=null)
			member.setIrum(dto.getIrum());
		
		if(dto.getTel()!=null)
			member.setTel(dto.getTel());
		
		if(dto.getEmail()!=null)
			member.setEmail(dto.getEmail());
		
		if(dto.getBirthday()!=null)
			member.setBirthday(dto.getBirthday());

		MultipartFile sajin = dto.getSajin();
		if(sajin!=null && sajin.isEmpty()==false) {
			String profile = ZmallUtil.saveProfile(sajin, member.getUsername());
			member.setProfile(profile);
		}
	}
	
	// 로그아운
	public void resign(String loginId) {
		dao.deleteById(loginId);
	}

	// 프로필 읽어오기
	public String getProfile(String username) {
		return dao.findById(username).get().getProfile();
	}
	
	// 권한 읽어오기
	public Authority readAuthority(Member member) {
		Authority authority = AuthDao.findByMember(member);
		return authority;
	}
	
	//주소 읽기
	public List<Read> addressRead(String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);	
		List<AddressDto.Read> address = member.getAddress().stream().map(a->a.toDto()).collect(Collectors.toList());
		return address;
	}
	//주소 추가
	public void addressAdd(String address, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		member.addAddressInfo(Arrays.asList(address));
		dao.save(member);
	}
	
	//펫 정보 읽기
	public List<Pet> petRead(String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);	
		List<Pet> pets = member.getPets().stream().collect(Collectors.toList());
		return pets;
	}
	//펫 정보 추가
	public void petAdd(PetDto.Add dto, String loginId) {
		Member member = dao.findById(loginId).orElseThrow(MemberFail.MemberNotFoundException::new);
		
		MultipartFile Petsajin = dto.getPetSajin();
		
		String petProfile = ZmallUtil.savePetProfile(Petsajin, member.getUsername(), dto.getPetName());
		member.setPets(dto.getPetName(), petProfile, dto.getPetAge(), dto.getGender(), dto.getPetKind());
		dao.save(member);
	}
	
	//전체 회원 수 읽기
	public Map<String,Long> readMemberCount() {
		long total = dao.count();
		long user = dslDao.countByUser();
		long admin = dslDao.countByAdmin();
		Map<String,Long> map = new HashMap<>();
		map.put("total", total);
		map.put("user", user);
		map.put("admin", admin);
		return map;
	}
	
	//보드 통계 읽기
	public Object readBoardCount() {
		long free = bDao.count();
		long photo = iDao.count();
		long QnA = qDao.count();
		Map<String,Long> map = new HashMap<>();
		map.put("free", free);
		map.put("photo", photo);
		map.put("QnA", QnA);
		return map;
	}

	//Role_user읽기
	public Map<String,Object> listUser(Integer pageno) {
		List<Member> member = dslDao.readByUser();
		Map<String,Object> map = new HashMap<>();
		map.put("content", member);
		map.put("totalcount", dslDao.countByUser());
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}
	
	//role_admin읽기
	public Map<String,Object> listAdmin(Integer pageno) {
		List<Member> member = dslDao.readByAdmin();
		Map<String,Object> map = new HashMap<>();
		map.put("content", member);
		map.put("totalcount", dslDao.countByAdmin());
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}

	public Boolean block(String username) {
		Member member = dao.findById(username).orElseThrow(MemberFail.MemberNotFoundException::new);	
		boolean now = member.getEnabled();
		member.setEnabled(!now);
		dao.save(member);
		return member.getEnabled();
	}

	public void addSeller(String busniess_no, String state, String loginId) {
		System.out.println(loginId);
		Seller seller = new Seller();
		seller.setUsername(loginId);
		seller.setState(state);
		seller.setBusniessNo(busniess_no);
		seller.setIsActive(false);
		
		sDao.save(seller);
	}

	public Map<String,Object>  listSeller(Integer pageno) {
		List<Seller> seller = (List<Seller>) sDao.findAll();
		Map<String,Object> map = new HashMap<>();
		map.put("content", seller);
		map.put("totalcount", sDao.countAll());
		map.put("pageno", pageno);
		map.put("pagesize", 10);
		return map;
	}

	public Boolean activeSeller(String username) {
		Seller seller = sDao.findById(username).orElseThrow(MemberFail.SellerNotFoundException::new);	
		seller.setIsActive(true);
		sDao.save(seller);		
		
		Member member = dao.findById(username).orElseThrow(MemberFail.MemberNotFoundException::new);
		Authority authority = new Authority();
		authority.setMember(member);
		authority.setAuthorityName("ROLE_SELLER");
		AuthDao.save(authority);
		return seller.getIsActive();
	}

	public Object inactiveSeller(String username) {
		Seller seller = sDao.findById(username).orElseThrow(MemberFail.SellerNotFoundException::new);	
		
		Member member = dao.findById(username).orElseThrow(MemberFail.MemberNotFoundException::new);
		Boolean isAlreadySeller = AuthDao.existsById(new AuthorityId(username, "ROLE_SELLER"));
		System.out.println(isAlreadySeller);
		if(isAlreadySeller==true) {
			AuthDao.deleteById(new AuthorityId(username, "ROLE_SELLER"));
			sDao.delete(seller);
		}else {
			sDao.delete(seller);
		}
	
		mailUtil.sendSellerMail("admin@zmall.com", member.getEmail());
		return null;
	}
	
}
