package member.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import member.bean.MemberDTO;
import member.dao.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private BCryptPasswordEncoder pwEncoder;
	@Autowired
	private JavaMailSenderImpl mailSender;

//	[시큐리티: 시큐리티에 사용자 정보 가져오기] ----------------------------------------------------------
	@Override
	public UserDetails loadUserByUsername(String mem_id) throws UsernameNotFoundException {
		MemberDTO memberDTO = memberDAO.getData(mem_id); //사용자 정보 가져오기
		
		if(memberDTO == null) { //사용자 정보 없으면 null 처리
			throw new UsernameNotFoundException(mem_id);
			
		} else { //사용자 정보 있으면 값 넣기
			memberDTO.setUsername(memberDTO.getMem_id()); //사용자 인증 가져오기
			memberDTO.setPassword(memberDTO.getMem_pwd());
			memberDTO.setAuthorities(memberDAO.getAuth(mem_id)); //사용자 권한 인증 가져오기
			
			return memberDTO;
		}
	}
	
//	[회원가입] ----------------------------------------------------------
	@Override
	//아이디 중복검사
	public String checkId(String id) {
		MemberDTO memberDTO = memberDAO.checkId(id);
		
		if(memberDTO == null) {//사용 가능			
			return "non_exist";
		}
		else //사용 불가
			return "exist";
	}
	@Override
	//이메일 중복검사
	public String checkEmail(String mem_email) {
		System.out.println("입력 이메일 확인:"+mem_email);
		MemberDTO memberDTO = memberDAO.checkEmail(mem_email);
		System.out.println("이메일 확인:"+memberDTO);
		if(memberDTO == null) {//사용 가능			
			return "non_exist";
		}
		else //사용 불가
			return "exist";
	}

	@Override
	//회원가입
	public int join(MemberDTO memberDTO) {
		//패스워드 암호화
		memberDTO.setMem_pwd(pwEncoder.encode(memberDTO.getMem_pwd()));
	
		return memberDAO.join(memberDTO);
	}
	
//	[로그인] ----------------------------------------------------------
	public void login(Map<String, String> map) {
		memberDAO.login(map);
	}
	@Override
	public void sessionLogin(Map<String, String> map, HttpSession session) {
		
		MemberDTO memberDTO = memberDAO.sessionLogin(map);
		session.setAttribute("memName", memberDTO.getMem_name());
		session.setAttribute("memId", memberDTO.getMem_id());
		session.setAttribute("memEmail", memberDTO.getMem_email());
		session.setAttribute("memKakao", memberDTO.getMem_kakao());
		System.out.println("세션 정보 : "+session.getAttribute("memId"));
		
	}
	
	@Override
	public String kakao(Map<String, String> map) {
		//카카오로 최초 로그인 시 회원가입 시키기
		if(memberDAO.getData(map.get("mem_id")) == null) {
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setMem_id(map.get("mem_id"));
			memberDTO.setMem_name(map.get("mem_name"));
			memberDTO.setMem_email(map.get("mem_id"));
			memberDTO.setEnabled(true);
			memberDTO.setAuthorities(Arrays.asList(new String[]{"ROLE_USER"}));
		
			memberDAO.joinKakao(memberDTO); 
		}
		return "success";
	}

	//회원 정보 가져오기(회원정보 수정 시, 기존 회원 데이터 뿌려주기)
	@Override
	public MemberDTO getData(String id) {
		return memberDAO.getData(id);
	}
	//회원정보수정
	@Override
	public void update(MemberDTO memberDTO) {
		memberDTO.setMem_pwd(pwEncoder.encode(memberDTO.getMem_pwd()));
		memberDAO.update(memberDTO);
	}
	//본인 재확인(마이페이지 들어가기 전)
	@Override
	public String  certify(Map<String, String> map) {
		MemberDTO memberDTO = memberDAO.getData(map.get("mem_id"));
		
		//pwEncoder.matches : (사용자가 입력한 패스워드(평문), 암호화된 패스워드)를 비교해주는 메소드
		if(pwEncoder.matches(map.get("mem_pwd"), memberDTO.getMem_pwd())) //인증 성공		
			return "yes";
		else 
			return "no";
	}
	//회원탈퇴
	@Override
	public void withdraw(Map<String, String> map) {
		memberDAO.withdraw(map);
	}
	//아이디 찾기
	@Override
	public MemberDTO findId(String mem_email) {
		MemberDTO memberDTO = memberDAO.findId(mem_email);
		
		if(memberDTO == null) {
			return null;
		}else {
			return memberDTO;
		}
	}
	//비밀번호찾기
	@Override
	public MemberDTO findPwd(Map<String, String> map) {
		MemberDTO memberDTO = memberDAO.findPwd(map);
		
		if(memberDTO == null) {
			return null;
		}else {
			return memberDTO;
		}
	}
	//비밀번호 재설정(아이디/비번찾기)
	@Override
	public void resetPwd(String mem_pwd, String mem_email) {
		Map <String, String> map = new HashMap<String, String>();
		map.put("mem_pwd", pwEncoder.encode(mem_pwd));
		map.put("mem_email", mem_email);
		memberDAO.resetPwd(map);
		
	}
	//카카오 회원 구분
	@Override
	public int distinguishKakao(String mem_id) {
		
		return memberDAO.distinguishKakao(mem_id);
	}
	//신고
	@Override
	public void complain(Map<String, String> map) {
		memberDAO.complain(map);
		
	}

	

	




	
}
