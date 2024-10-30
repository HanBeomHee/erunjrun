package com.erunjrun.admin.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.erunjrun.admin.dto.AdminDTO;
import com.erunjrun.image.dto.ImageDTO;

@Mapper
public interface AdminDAO {
	
// login,join	
	public String getAllowedIp(String id);

	public boolean adminLogin(String id, String pw); 
	   
	public String getAuthority(String id);
	 
	public boolean adminJoin(String id, String pw, String name, String ip);

	String SgetAllowedIp(String superAdminId);
	
	
//	회원리스트
	public List<AdminDTO> memberlist(String opt, String keyword,String sortField,String sortOrder, int limit, int offset);
	
	int count(int cnt);

	
//	관리자리스트
	public List<AdminDTO> adminlist(String opt, String keyword, int limit, int offset);

	int admincount(int cnt_);

	
//	회원상세페이지
	public AdminDTO memberdetail(String id);

	public List<AdminDTO> memberreportlist(String id);
	
	public List<AdminDTO> ban(String id);

//	권한	
	public String right(String nickname);
	
	public int rightwrite(Map<String, String> param);
	
	public AdminDTO rightdetail(String ban_idx);

	public void rightupdate(Map<String, String> param);
	
	 // 시작일이 오늘 또는 과거인 권한을 모두 'Y'로 업데이트
    void updateRightY(Date sqlDate);

    // 종료일이 오늘 또는 과거인 권한을 모두 'N'으로 업데이트
    void updateRightN(Date endDate);
	
// 신고	
	public List<AdminDTO> reportlist(int limit, int offset,String category);

	public int reportcount(int cnt_);

	public AdminDTO reportdetail(String report_idx);

	public ImageDTO image(String report_idx);

	public void reportupdate(Map<String, String> param);
	
	
// 문의하기
	public List<AdminDTO> asklist(String opt, String keyword, int limit, int offset);

	public int askcount(int cnt_);
	
	
	
// 태그
	public List<AdminDTO> taglist(int limit, int offset);
	
	public int tagcount(int cnt_);

	public void tagwrite(Map<String, String> param);

	public AdminDTO tagdetail(String tag_idx);

	public void tagupdate(Map<String, String> param);

	
	
// 구분코드	
	public List<AdminDTO> codelist(int limit, int offset, String opt, String keyword);
	
	public int codecount(int cnt_);

	public void codewrite(Map<String, String> param);

	public int codeoverlay(String code_name);

	public AdminDTO codedetail(String code_name);

	public void codeupdate(Map<String, String> param);
	
	
	
	
	// 팝업
	public List<AdminDTO> pupuplist(int limit, int offset);

	public int pupupcount(int cnt_);
	
	



	




}

