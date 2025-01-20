package com.erunjrun.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erunjrun.member.dto.MemberDTO;
import com.erunjrun.admin.dto.RightDTO;
import com.erunjrun.config.AesConfig;
import com.erunjrun.member.dao.MemberDAO;

@Service
public class MemberService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MemberValidationService memberValidationService; // 유효성 검사를 위한 서비스
	@Autowired
	MemberDAO memberDAO;
	@Autowired
	PasswordEncoder encoder; // pw 암호화
	@Autowired
	AesConfig aesConfig; // 주소, 이메일, 전화번호 암호화

	// 회원가입
	public boolean join(MemberDTO params) {
		if (memberValidationService.validateParams(params)) {
			boolean success = false;
			params.setPw(encoder.encode(params.getPw())); // 비밀번호 암호화
			try {
				 if (params.getAddress() != null) {
					params.setAddress(AesConfig.encrypt(params.getAddress())); // 주소 암호화
				 }
				 if (params.getEmail() != null) {
					params.setEmail(AesConfig.encrypt(params.getEmail())); // 이메일 암호화
				 }
				 if (params.getPhone() != null) {
					params.setPhone(AesConfig.encrypt(params.getPhone())); // 전화번호 암호화
				 }
				 if (memberDAO.join(params) > 0) {
					success = true;
				 }
			} catch (Exception e) {
				e.printStackTrace();
				return success;
			  }
		}
		return false;
	}

	public Object idOverlay(String id) {
		return memberDAO.idOverlay(id);
	}

	public int nickNameOverlay(String nickName) {
		return memberDAO.nickNameOverlay(nickName);
	}

	public int emailOverlay(String email) {
		return memberDAO.emailOverlay(email);
	}

	public boolean login(String id, String pw) {

		boolean success = false;
		if (memberDAO.login(id, pw) > 0) {
			success = true;
		}
		return success;
	}

	public MemberDTO findSessionId(String id) {
		return memberDAO.findSessionId(id);
	}

	public MemberDTO findId(MemberDTO dto) {
		return memberDAO.findId(dto);
	}

	public MemberDTO findPw(MemberDTO dto) {
		return memberDAO.findPw(dto);
	}

	public void pwUpdate(String id, String tempPw) {
		memberDAO.pwUpdate(id, tempPw);
	}

	public RightDTO isBan(String id) {
		return memberDAO.isBan(id);
	}

}
