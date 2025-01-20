package com.erunjrun.member.service;

import org.springframework.stereotype.Service;

import com.erunjrun.member.dto.MemberDTO;

@Service
public class MemberValidationService {

	public boolean validateParams(MemberDTO params) {
				String id = params.getId();
				String pw = params.getPw();
				String email = params.getEmail();
				String phoneNumber =  params.getPhone();
				
				return validationMemberId(id)
					&& validationMemberPw(pw) 
					&& validationMemberEmail(email) 
					&& validationPhoneNumber(phoneNumber);
	}
	
	public boolean validationMemberId(String id) {
		// ID 검사 로직 작성
		return id != null && id.length() >= 3;
	}
	
	public boolean validationMemberPw(String pw) {
		// PW 검사 로직 작성 
		return pw != null && pw.length() >= 8;
	}
	
	public boolean validationMemberEmail(String email) {
		// 추가적인 email 유효성 검사 로직...?
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return email != null && email.matches(emailRegex);
	}
	
	public boolean validationPhoneNumber(String phoneNumber) {
		// 핸드폰 검사 로직 작성
		String phoneRegex = "^010-\\d{4}-\\d{4}$";
		return phoneNumber != null && phoneNumber.matches(phoneRegex);
	}
		
}
