package com.erunjrun.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erunjrun.member.service.MailService;
import com.erunjrun.member.service.MemberService;
import com.erunjrun.admin.dto.RightDTO;
import com.erunjrun.member.dto.MemberDTO;

@Controller
public class MemberController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MemberService memberService;	
	@Autowired
	MailService mailService;

	// 회원가입 페이지 이동
	@GetMapping(value = "/joinView")
	public String joinView() {
		return "member/join";
	}
	
	// 회원가입
	@PostMapping(value = "/join")
	public String join(Model model, @RequestParam MemberDTO params) {
		String page = "member/join";
		if (memberService.join(params)) {
			model.addAttribute("msg", "회원가입에 성공했습니다!");
			page = "member/login";
		} else {
			model.addAttribute("msg", "회원가입에 실패했습니다.");
		}
		return page;
	}
	
	// 로그인 페이지 이동
	@GetMapping(value = "/loginView")
	public String loginView() {
		return "member/login";
	}

	// 로그인
	@PostMapping(value = "/login")
	public String login(Model model, HttpSession session, String id, String pw) {
		MemberDTO member = memberService.findSessionId(id); // 대소문자 구분하여 조회
		if (member == null) {
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인해주세요.");
		} //else if ("Y".equals(member.getUse_yn())) { // 탈퇴 상태 체크
			//model.addAttribute("msg", "탈퇴된 회원입니다."); 
		//} 
		  else if (memberService.isBan(id) != null) { // 벤 상태 체크
			RightDTO rightdto = memberService.isBan(id);
			String msg = rightdto.getEnd_date() + "일 까지 정지된 회원입니다.";
			model.addAttribute("msg", msg);
			return "member/login";
		} else if (member.getId().equals(id) && memberService.login(id, pw)) {
			model.addAttribute("msg", "로그인 되었습니다!");
			session.setAttribute("loginId", id);
		} else {
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인해주세요.");
		}

		return "main";
	}

	@GetMapping(value = "/logOut")
	public String logout(HttpSession session, Model model) {
		session.invalidate(); // 세션 전체를 무효화 (세션에 저장된 모든 정보가 삭제됨)
		model.addAttribute("msg", "로그아웃 되었습니다.");
		return "member/login"; // 로그인 페이지로 리다이렉트
	}

	@GetMapping(value = "/idOverlay")
	@ResponseBody
	public Map<String, Object> getIdOverlay(String id) {
		logger.info("overlay check : " + id);
		Map<String, Object> map = new HashMap<>();
		map.put("overlay", memberService.idOverlay(id));
		return map;
	}

	@GetMapping(value = "/nickNameOverlay")
	@ResponseBody
	public Map<String, Object> getNickNameOverlay(String nickName) {
		logger.info("nickName check: " + nickName);
		Map<String, Object> map = new HashMap<>();
		map.put("overlay", memberService.nickNameOverlay(nickName));
		return map;
	}

	// 이메일 중복 확인
	@GetMapping(value = "/emailOverlay")
	@ResponseBody
	public Map<String, Object> getEmailOverlay(@RequestParam String email) {
		Map<String, Object> map = new HashMap<>();
		map.put("overlay", memberService.emailOverlay(email));
		return map;
	}
	
	// 인증번호 보내기
	@PostMapping(value = "/sendVerificationCode")
	@ResponseBody
    public Map<String, Object> emailOverlay(@RequestParam String email) {
		Map<String, Object> response = new HashMap<>();
        String verificationCode = mailService.sendVerificationCode(email);
        if (verificationCode != null) {
			response.put("success", true);
		} else {
			response.put("success", false);
		}
        return response;
	}
	
	// 인증번호 확인
	@PostMapping(value = "/verifyCode")
	@ResponseBody
    public Map<String, Object> verifyCode(@RequestParam String email, @RequestParam String verificationCode) {
		Map<String, Object> response = new HashMap<>();
		boolean valid = mailService.verifyCode(email, verificationCode);
		response.put("valid", valid);
		return response;
	}
	
	@GetMapping(value = "/findIdView")
	public String findIdview() {
		return "member/findId";
	}

	@PostMapping(value = "/findId")
	public String findMemberId(HttpServletRequest request, Model model, MemberDTO dto, @RequestParam String name,
			@RequestParam String email) {
		dto.setName(name);
		dto.setEmail(email);
		MemberDTO id = memberService.findId(dto);
		if (id == null) {
			model.addAttribute("msg", "이름 및 이메일이 일치하는 id가 없습니다.");
		}
		model.addAttribute("findId", id);
		return "member/findIdResult";
	}

	@GetMapping(value = "/findPwView")
	public String findpwView() {
		return "member/findPw";
	}

	@PostMapping(value = "/tempPw") // 임시비밀번호 부분입니다.
	public String tempPw(HttpServletRequest request, Model model, MemberDTO dto, @RequestParam String id,
			@RequestParam String name, @RequestParam String email) {
		dto.setId(id);
		dto.setName(name);
		dto.setEmail(email);

		MemberDTO pw = memberService.findPw(dto);
		if (pw != null) {
			String tempPw = UUID.randomUUID().toString().substring(0, 8);
			memberService.pwUpdate(id, tempPw);
			model.addAttribute("tempPw", tempPw);
			model.addAttribute("msg", "임시 비밀번호가 생성되었습니다.");
		} else {
			model.addAttribute("msg", "입력한 정보와 일치하는 계정이 없습니다.");
		}
		return "member/findPwResult";
	}

}
