package com.erunjrun.admin.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.erunjrun.admin.dao.AdminDAO;
import com.erunjrun.admin.dto.AdminDTO;
import com.erunjrun.admin.dto.PopupDTO;
import com.erunjrun.image.dto.ImageDTO;



@Service
public class AdminService {

   Logger logger = LoggerFactory.getLogger(getClass());
   
   @Autowired AdminDAO admin_dao;

   public String getAllowedIp(String id) {
        return admin_dao.getAllowedIp(id);
    }

   public boolean adminLogin(String id, String pw) {
      return admin_dao.adminLogin(id, pw);
   }
   
   public String getAuthority(String id) {
       return admin_dao.getAuthority(id);
   }
   
   public String SgetAllowedIp(String superAdminId) {
         return admin_dao.getAllowedIp(superAdminId);
         
      }

   public boolean adminJoin(String id, String pw, String name,String ip) {
      
         return admin_dao.adminJoin(id,pw,name,ip);
      }
   
   
   public int count(int cnt_) {
      
      return admin_dao.count(cnt_);
   }


   public List<AdminDTO> memberlist(String opt, String keyword, String sortField,String sortOrder,int limit, int offset) {
      
      return admin_dao.memberlist(opt, keyword, sortField,sortOrder,limit, offset);
   }

   public int admincount(int cnt_) {
   
      return admin_dao.admincount(cnt_);
   }

   public List<AdminDTO> adminlist(String opt, String keyword, int limit, int offset) {
   
      return admin_dao.adminlist(opt, keyword, limit, offset);
   }


   public List<AdminDTO> memberreportlist(String id) {
      
      return admin_dao.memberreportlist(id);
   }

   public AdminDTO memberdetail(String id) {
      
      return admin_dao.memberdetail(id);
   }
   
   
//   권한처리 - 게시글등록,댓글등록,로그인 할때 session에 권한 저장해서 가져오기

   public List<AdminDTO> ban(String id) {
      
      return admin_dao.ban(id);
   }
   
   public String right(String nickname) {
      
      return admin_dao.right(nickname);
   }

   public void rightwrite(Map<String, String> param) {
      admin_dao.rightwrite(param);
   }

   public AdminDTO rightdetail(String ban_idx) {
      
      return admin_dao.rightdetail(ban_idx);
   }

   public void rightupdate(Map<String, String> param) {
      admin_dao.rightupdate(param);
      
   }
   
   @PostConstruct
    public void init() {
        logger.info("서버 시작 시 권한 업데이트 실행");
        checkAndUpdateStatus(); // 서버가 시작될 때 권한 업데이트
    }
   
   
   
   @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void checkAndUpdateStatus() {
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        try {
            // 1. 오늘 또는 과거의 시작일에 해당하는 권한을 'Y'로 변경
            admin_dao.updateRightY(sqlDate);
            System.out.println("지난 시작일 포함 모든 권한을 'Y'로 변경했습니다.");

            // 2. 오늘 또는 과거의 종료일에 해당하는 권한을 'N'으로 변경
            admin_dao.updateRightN(sqlDate);
            System.out.println("지난 종료일 포함 모든 권한을 'N'으로 변경했습니다.");
        } catch (Exception e) {
            System.err.println("스케줄러 실행 중 오류 발생: " + e.getMessage());
        }
    }
   
   
   
//   신고 - 신고게시글 번호 누르면 신고 상세페이지로 이동해야함.
   public List<AdminDTO> reportlist(int limit, int offset, String category, String status) {
      
      return admin_dao.reportlist(limit,offset,category,status);
   }

   public int reportcount(int cnt_) {
      
      return admin_dao.reportcount(cnt_);
   }

   public void reportdetail(String report_idx, String code_name, Model model) {
	   logger.info("report_idx"+report_idx);
	   logger.info("code_name"+code_name);
      AdminDTO dto = admin_dao.reportdetail(report_idx);
      ImageDTO imageDTO = admin_dao.image(report_idx,code_name); // 이미지 정보 가져오기
      logger.info(""+imageDTO);
      model.addAttribute("file",imageDTO);
      model.addAttribute("info",dto);
      
      
   }

   public void reportupdate(Map<String, String> param) {
      admin_dao.reportupdate(param);
      
   }
   
   
   
   //문의하기 - 경로설정 누르면 상세페이지로 이동하기

   public List<AdminDTO> asklist(String opt, String keyword, int limit, int offset) {
      
      return admin_dao.asklist(opt, keyword ,limit, offset);
   }
   
   public int askcount(int cnt_) {
      
      return admin_dao.askcount(cnt_);
   }

   
 //태그   
   
   public List<AdminDTO>taglist(int limit, int offset) {
      
      return admin_dao.taglist(limit, offset);
   }

   public int tagcount(int cnt_) {
   
      return admin_dao.tagcount(cnt_);
   }

   public void tagwrite(Map<String, String> param) {
      admin_dao.tagwrite(param);
      
   }

   public void tagdetail(String tag_idx,Model model) {
      AdminDTO dto = admin_dao.tagdetail(tag_idx);
      model.addAttribute("info",dto);
      
      
   }

   public void tagupdate(Map<String, String> param) {
      admin_dao.tagupdate(param);
      
   }

   
   
   
   
   
// 구분코드   
   public int codecount(int cnt_) {
      
      return admin_dao.codecount(cnt_);
   }


   public List<AdminDTO> codelist(int limit, int offset, String opt, String keyword) {
      
      return admin_dao.codelist(limit, offset,opt,keyword);
   }
   
   public void codewrite(Map<String, String> param) {
      admin_dao.codewrite(param);
         
   
   }

   public int codeoverlay(String code_name) {
      
      return admin_dao.codeoverlay(code_name);
   }

   public void codedetail(String code_name, Model model) {
      AdminDTO dto = admin_dao.codedetail(code_name);
      model.addAttribute("info",dto);
      
   }

   public void codeupdate(Map<String, String> param) {
      admin_dao.codeupdate(param);
      
   }

   
   
// 팝업리스트
   
   public int popupcount(int cnt_) {
      
      return admin_dao.popupcount(cnt_);
   }

   public List<PopupDTO> popuplist(int limit, int offset) {
      
      return admin_dao.popuplist(limit,offset);
   }

   
   public String popupwrite(MultipartFile file, Map<String, String> param) {
   
      PopupDTO dto = new PopupDTO();
      
      dto.setSubject(param.get("subject"));
      dto.setPriority(param.get("priority"));
      dto.setUse_yn(param.get("use_yn"));
      dto.setCode_name("code_name");
      dto.setX(param.get("x"));
      dto.setY(param.get("y"));
      dto.setWidth(param.get("width"));
      dto.setHeight(param.get("height"));
      dto.setContent(param.get("content"));
      dto.setCode_name(param.get("code_name"));
      
      logger.info(param.get("code_name"));
      String start =  param.get("start_date");
      String end =  param.get("end_date");
            try {
              Date start_date = Date.valueOf(start);
              Date end_date = Date.valueOf(end);
            dto.setStart_date(start_date);
            dto.setEnd_date(end_date);
         } catch (Exception e) {
            e.printStackTrace();
         } 
      
            String newPriority = param.get("priority");
            admin_dao.ExistingPopupPriority(newPriority);

            // 새 팝업 등록
            admin_dao.popupwrite(dto);

            int popup_idx = dto.getPopup_idx();
            String code_name = dto.getCode_name();
            if (file != null) {
                logger.info("파일");
            } else {
                logger.info("없음");
            }
            if (popup_idx > 0) {
                fileSave(file, popup_idx, code_name);
            }

            return "redirect:/adminPopup";
        }
   
   
      
   

   public void fileSave(MultipartFile file, int popup_idx,String code_name) {
         
      if (file != null) {
         
         try {
            String img_ori = file.getOriginalFilename();
            int pos = img_ori.lastIndexOf(".");
            if (pos>=0) {
            String ext = img_ori.substring(pos);
            String img_new = UUID.randomUUID()+ext;
            byte[] arr = file.getBytes();
            Path path = Paths.get("C:/upload/"+img_new);
            Files.write(path, arr);
            int row = admin_dao.fileWrite(img_ori,img_new,popup_idx,code_name);
            logger.info(""+row);
            }
         }catch (Exception e) {
            
            e.printStackTrace();
         }
      }else {
         admin_dao.filedelete(popup_idx,code_name);
      }
         
      
   }

   public void popupdetail(String popup_idx, String code_name, Model model) {
      
      model.addAttribute("info",admin_dao.popupdetail(popup_idx,code_name));
      ImageDTO imageDTO = admin_dao.image(popup_idx,code_name);
      model.addAttribute("file",imageDTO);
   }

   public void popupupdate(MultipartFile file, Map<String, String> param) {
       admin_dao.popupupdate(param);
      
      String code_name = param.get("code_name");
      String popup_idx =param.get("popup_idx");
      int idx = Integer.parseInt(param.get("popup_idx"));
       String deleteImageFlag = param.get("deleteImage");
          if ("Y".equals(deleteImageFlag)) {
              admin_dao.filedelete(idx, code_name);
          }
          String newPriority = param.get("priority");
            admin_dao.ExistingPopupPriority(newPriority);

          
          // 2. 팝업 정보 업데이트
          admin_dao.popupupdate(param);
          
          // 3. 새 이미지가 있는 경우 파일 업데이트
          if (file != null && !file.isEmpty()) {
              ImageDTO imageDTO = admin_dao.image(popup_idx, code_name);
              if (imageDTO != null) {
                  // 기존 이미지가 있으면 업데이트
                  fileupdate(file, popup_idx, code_name);
              } else {
                  // 기존 이미지가 없으면 새로 저장
                  fileSave(file, idx, code_name);
              }
          }
        
      
      
   }



   
   public void fileupdate(MultipartFile file, String popup_idx,String code_name) {
      try {
         String img_ori = file.getOriginalFilename();
         int pos = img_ori.lastIndexOf(".");
         if (pos>=0) {
         String ext = img_ori.substring(pos);
         String img_new = UUID.randomUUID()+ext;
         byte[] arr = file.getBytes();
         Path path = Paths.get("C:/upload/"+img_new);
         Files.write(path, arr);
         int row = admin_dao.fileupdate(img_ori,img_new,popup_idx,code_name);
         logger.info(""+row);
         }
      }catch (Exception e) {
         
         e.printStackTrace();
      }
      
   
}

   public int filedelete(String code_name, int popup_idx) {
      
      return admin_dao.filedelete(popup_idx, code_name);
   }

   public void adminyn(String admin_id) {
      admin_dao.adminyn(admin_id);
      
   }

   
   

   
   

   

   


   
    

   
}