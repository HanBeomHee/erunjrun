package com.erunjrun.member.dao;

import org.apache.ibatis.annotations.Mapper;

import com.erunjrun.admin.dto.RightDTO;
import com.erunjrun.member.dto.MemberDTO;

@Mapper
public interface MemberDAO {

	int join(MemberDTO params); // 회원가입

	int idOverlay(String id);

	int nickNameOverlay(String nickName);

	int emailOverlay(String email);

	int login(String id, String pw);
	
	MemberDTO findSessionId(String id);

	MemberDTO findId(MemberDTO dto);

	MemberDTO findPw(MemberDTO dto);

	void pwUpdate(String id, String tempPw);

	RightDTO isBan(String id);	
}
