package com.erunjrun.mypage.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.erunjrun.mate.dto.MateProfileDTO;
import com.erunjrun.mate.service.MateService;
import com.erunjrun.member.dto.MemberDTO;
import com.erunjrun.member.dto.ProfileDTO;
import com.erunjrun.mypage.dto.MypageDTO;
import com.erunjrun.mypage.service.MypageService;

@Controller
public class MypageController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired MypageService mypageService;

	// 이거는 암호화된 요소들 복호화할 때 쓴다.
	//public String getDecryptedEmail(String encryptedEmail) throws Exception {
	//    return AESUtil.decrypt(encryptedEmail); // 암호화된 이메일 복호화
	//}
	
	@GetMapping(value = "/profileDetail")
	public String profileDetail(Model model, HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			MemberDTO member = mypageService.profileDetail(id);
			ProfileDTO profile = mypageService.ProfileImage(id);
			model.addAttribute("member", member);
			model.addAttribute("profile", profile);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 이동
		}
		model.addAttribute("pageName", "profileDetail");
		return "mypage/profile";
	}

	@GetMapping(value = "/profileUpdateView")
	public String profileUpdateView(HttpSession session, Model model) {

		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			MemberDTO member = mypageService.profileDetail(id);
			ProfileDTO profile = mypageService.ProfileImage(id);
			model.addAttribute("member", member);
			model.addAttribute("profile", profile);
			model.addAttribute("pageName", "profileDetail");
			return "mypage/profileUpdate"; // 수정 페이지로 이동
		} else {
			return "main"; // 로그인 페이지로 리다이렉트
		}
	}

	@PostMapping(value = "/profileUpdate")
	public String profileUpdate(Model model, @RequestParam Map<String, String> params, HttpSession session) {

		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			params.put("id", id); // 세션에서 가져온 ID를 params에 추가

			// 업데이트 호출
			mypageService.profileUpdate(params);

			// 사용자 정보 및 프로필 이미지 가져오기
			MemberDTO member = mypageService.profileDetail(id);
			ProfileDTO profile = mypageService.ProfileImage(id);
			model.addAttribute("profile", profile);
			model.addAttribute("member", member);

			return "mypage/profile"; // 프로필 페이지로
		} else {
			// 세션에 ID가 없으면 로그인 페이지로
			return "member/login";
		}
	}

	@GetMapping(value = "/deleteView")
	public String deleteView() {
		return "mypage/delete";
	}

	@PostMapping("/memberDelete") // 실제로 delete하는 것이 아니니, 혼동 방지를 위해 @post로 작성했습니다!
	public String memberDelete(@RequestParam String id, @RequestParam String email, @RequestParam String pw,
			HttpSession session, Model model) {

		String sessionId = (String) session.getAttribute("loginId");
		if (sessionId.equals(id)) {
			// ID가 세션에 있는지 확인
			MemberDTO member = mypageService.findSessionId(id);
			if (member.getEmail().equals(email) && member.getPw().equals(pw)) {
				// 이메일과 비밀번호가 일치하는 경우
				mypageService.setUseY(id); // 탈퇴 상태로 변경
				session.invalidate(); // 세션 무효화
				model.addAttribute("msg", "탈퇴가 완료되었습니다. 지금까지 이용해주셔서 감사합니다.");
				return "main"; // 로그인 페이지로 리다이렉트
			} else {
				model.addAttribute("msg", "입력한 정보가 올바르지 않습니다.");
			}
		} else {
			model.addAttribute("msg", "로그인후 탈퇴할 수 있습니다.");
		}
		return "main"; // 탈퇴 페이지로 다시 리다이렉트
	}

	@GetMapping(value = "/createExerciseProfile")
	public String createExerciseProfile(HttpSession session, Model model) {
		String id = (String) session.getAttribute("loginId");

		if (id != null) {
			// 회원 정보 조회
			MemberDTO member = mypageService.findSessionId(id);
			ProfileDTO profile = mypageService.ProfileImage(id);

//			if ("Y".equals(member.getProfile_use())) {
//				MypageDTO mypage = mypageService.mypageDetail(id);
//				model.addAttribute("mypage", mypage); // 모델에 MypageDTO 추가
//				// 운동 프로필이 이미 작성된 경우
//				return "redirect:/ExerciseProfile"; // 운동 프로필 페이지로 리다이렉트
//			}
			model.addAttribute("profile", profile);
			model.addAttribute("loginId", id);
			model.addAttribute("member", member); // 모델에 MypageDTO 추가
			String profileImage = (String) session.getAttribute("profileImage");
			model.addAttribute("profileImage", profileImage);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		model.addAttribute("pageName", "ExerciseProfile");
		return "mypage/createExerciseProfile"; // 최초 프로필 작성 안내 페이지로 이동
	}

	@GetMapping(value = "/firstExerciseProfileView")
	public String firstExerciseProfileView(HttpSession session, Model model) {
		String id = (String) session.getAttribute("loginId");

		if (id != null) {
			// 회원 정보 조회
			MemberDTO member = mypageService.profileDetail(id); // 여기서 회원 정보를 가져옴
			ProfileDTO profile = mypageService.ProfileImage(id);
//			if ("Y".equals(member.getProfile_use())) {
//				// 운동 프로필이 이미 작성된 경우
//				return "mypage/ExerciseProfile"; // 운동 프로필 페이지로 리다이렉트
//			}
			model.addAttribute("profile", profile);
			model.addAttribute("loginId", id);
			model.addAttribute("member", member); // member를 모델에 추가
			logger.info("Member Birth: {}", member.getBirth());
			String birthString = member.getFormattedBirth();
			model.addAttribute("birthString", birthString); // 포맷된 생년월일 추가

			String profileImage = (String) session.getAttribute("profileImage");
			model.addAttribute("profileImage", profileImage);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		model.addAttribute("pageName", "ExerciseProfile");
		return "mypage/firstExerciseProfile"; // 최초 프로필 작성 안내 페이지로 이동
	}

	@PostMapping(value = "/firstExerciseProfile")
	public String firstExerciseProfile(@RequestParam Map<String, String> params,
			@RequestParam("imageFile") MultipartFile imageFile, HttpSession session, Model model) {
		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			// 이미지 파일 처리
			if (!imageFile.isEmpty()) {
				String originalFileName = imageFile.getOriginalFilename();
				String newFileName = UUID.randomUUID().toString() + "_" + originalFileName; // 새로운 파일 이름 생성

				// 파일 저장 경로
				String uploadDir = "C:/upload/"; // 실제 경로
				Path path = Paths.get(uploadDir + newFileName);

				try {
					Files.write(path, imageFile.getBytes()); // 파일 저장
					params.put("image", newFileName); // params에 이미지 파일 이름 추가
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("msg", "파일 저장 중 오류가 발생했습니다.");
					return "mypage/createExerciseProfile"; // 에러 페이지로 리다이렉트
				}
			}

			// 운동 프로필 생성
			mypageService.profileUpdate(params); // 업데이트 호출
			ProfileDTO profile = mypageService.ProfileImage(id);
			model.addAttribute("profile", profile);
			mypageService.firstExerciseProfile(params);
			logger.info("Params: {}", params);

			// 프로필 작성 상태 업데이트
			mypageService.updateProfile_use(id, "Y");

			// 프로필 공개 여부 및 운동 메이트 찾기 여부 업데이트
			String profileVisibility = params.get("profileVisibility");
			String mateSearch = params.get("mateSearch");

			// 프로필 공개 여부 업데이트
			mypageService.updateProfileVisibility(id, "Y".equals(profileVisibility) ? "Y" : "N");

			// 운동 메이트 찾기 여부 업데이트
			mypageService.updateMateSearch(id, "Y".equals(mateSearch) ? "Y" : "N");

			// 포인트 추가
			mypageService.addPoint(id, 10);

			// 업데이트된 회원 정보 가져오기
			MemberDTO member = mypageService.profileDetail(id);
			String birthString = member.getFormattedBirth();
			model.addAttribute("birthString", birthString); // 포맷된 생년월일 추가
			model.addAttribute("member", member);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		return "redirect:/ExerciseProfile";
	}

	@GetMapping(value = "/ExerciseProfile")
	public String ExerciseProfile(Model model, HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			MemberDTO member = mypageService.profileDetail(id);
			ProfileDTO profile = mypageService.ProfileImage(id);
			MypageDTO mypage = mypageService.mypageDetail(id);

			// 생년월일을 가져와 로그에 출력
			logger.info("Member Birth: {}", member.getBirth());

			// 포맷된 생년월일 추가
			String birthString = member.getFormattedBirth();
			model.addAttribute("member", member);
			model.addAttribute("profile", profile);
			model.addAttribute("mypage", mypage); // 모델에 MypageDTO 추가
			model.addAttribute("birthString", birthString); // 포맷된 생년월일 추가

		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 이동
		}
		model.addAttribute("pageName", "ExerciseProfile");
		return "mypage/ExerciseProfile";
	}

	@GetMapping(value = "/ExerciseProfileUpdateView")
	public String ExerciseProfileUpdateView(Model model, HttpSession session) {
		String id = (String) session.getAttribute("loginId");
		if (id != null) {
			MemberDTO member = mypageService.profileDetail(id);
			MypageDTO mypage = mypageService.mypageDetail(id);
			ProfileDTO profile = mypageService.ProfileImage(id);

			String birthString = member.getFormattedBirth();
			model.addAttribute("birthString", birthString); // 포맷된 생년월일 추가
			model.addAttribute("member", member);
			model.addAttribute("profile", profile);
			model.addAttribute("mypage", mypage); // 모델에 MypageDTO 추가
			model.addAttribute("profileVisibility", mypage.getProfile_use());
			model.addAttribute("mateSearch", mypage.getExercise_use());
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login";
		}
		model.addAttribute("pageName", "ExerciseProfile");
		return "mypage/ExerciseProfileUpdate";
	}

	@PostMapping(value = "/ExerciseProfileUpdate")
	public String ExerciseProfileUpdate(Model model, HttpSession session, @RequestParam Map<String, String> params,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
		logger.info("Received params: {}", params);
		logger.info("Image parameter added to params: {}", params.get("image"));
		String id = (String) session.getAttribute("loginId");

		if (id != null) {
			// 이미지 파일 처리
			if (imageFile != null && !imageFile.isEmpty()) {

				String originalFileName = imageFile.getOriginalFilename();
				String newFileName = UUID.randomUUID().toString() + "_" + originalFileName; // 새로운 파일 이름 생성

				// 파일 저장 경로
				String uploadDir = "C:/upload/"; // 실제 경로
				Path path = Paths.get(uploadDir + newFileName);

				try {
					Files.write(path, imageFile.getBytes()); // 파일 저장
					params.put("image", newFileName); // params에 이미지 파일 이름 추가
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("msg", "파일 저장 중 오류가 발생했습니다.");
					return "mypage/createExerciseProfile"; // 에러 페이지로 리다이렉트
				}
			}

			// 프로필 정보 업데이트
			mypageService.profileUpdate(params); // 업데이트 호출
			mypageService.ExerciseProfileUpdate(params);
			String profileVisibility = params.get("profileVisibility");
			String mateSearch = params.get("mateSearch");
			mypageService.updateProfileVisibility(id, "Y".equals(profileVisibility) ? "Y" : "N");
			mypageService.updateMateSearch(id, "Y".equals(mateSearch) ? "Y" : "N");

			model.addAttribute("msg", "프로필이 성공적으로 업데이트되었습니다.");
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login"; // 로그인 페이지로 리다이렉트
		}

		return "redirect:/ExerciseProfile"; // 업데이트 후 프로필 페이지로 리다이렉트
	}

	@GetMapping(value = "/pointHistoryListView")
	public String pointHistoryListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("member", member);
		model.addAttribute("profile", profile);
		model.addAttribute("pageName", "pointHistoryListView");
		return "mypage/pointHistoryList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/pointHistoryList.ajax")
	@ResponseBody
	public Map<String, Object> list(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.list(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/myMateListView")
	public String myMateListView(HttpSession session, Model model) {
		logger.info("myMateListView 요청 수신됨"); // 로그 추가
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return "member/login";
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myMateListView");
		return "mypage/myMateList";
	}

	@GetMapping(value = "/myMateList.ajax")
	@ResponseBody
	public Map<String, Object> myMateList(String page, String cnt, HttpSession session, Model model) {
		logger.info("myMateList.ajax 요청 수신됨, 페이지: {}, 카운트: {}", page, cnt); // 로그 추가
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return null; // 에러 응답 반환
		}

		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		Map<String, Object> result = mypageService.myMateList(page_, cnt_, id);
		logger.info("Service에서 반환된 결과: {}", result); // 로그 추가
		return result;
	}

	@RequestMapping(value = "/myMateDelete.ajax")
	@ResponseBody
	public Map<String, Object> myMateDelete(@RequestParam String mateIdx, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    String currentUserId = (String) session.getAttribute("loginId");  // 세션에서 로그인한 사용자 ID 가져오기
	    try {
	        logger.info("Deleting mate relationship for mateIdx: " + mateIdx);
	        boolean success = mypageService.myMateDelete(mateIdx, currentUserId);  // 로그인 ID도 함께 전달

	        if (success) {
	            response.put("success", true);
	        } else {
	            response.put("success", false);
	            response.put("message", "메이트 끊기 실패");
	        }
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "서버 오류: " + e.getMessage());
	    }
	    return response;
	}
	
	// 친구 요청 페이지로 이동
	@GetMapping(value = "/requestedMateListView")
	public String requestedMateListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myMateListView");
		return "mypage/requestedMateList"; // JSP 페이지 이름
	}

	@GetMapping("/friendRequests.ajax")
	@ResponseBody // JSON 형식으로 응답
	public Map<String, Object> getFriendRequests(String page, String cnt, HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // 에러 응답 반환
		}

		// 친구 요청 가져오기 (ID를 매개변수로 전달)
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.getFriendRequests(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@Autowired
    private MateService mateService; // MateService는 적절한 서비스로..

    @RequestMapping(value="mypage/mate/{toUserId}")
    public String getMateProfile(@PathVariable String toUserId, HttpSession session, Model model) {
        // 세션에서 사용자 ID 가져오기
        String fromUserId = (String) session.getAttribute("loginId");

        // 운동메이트 신청 여부 체크
        Map<String, Object> result = new HashMap<>();
        result.put("MateAppl", mateService.checkMateAppl(fromUserId, toUserId));

        // 차단 여부 체크
        result.put("isBlocked", mateService.checkBlock(fromUserId, toUserId));

        // 좋아요 여부 체크
        result.put("isLiked", mateService.checkLike(fromUserId, toUserId));

        // 프로필 데이터 가져오기
        MateProfileDTO profileDto = mateService.getProfile(toUserId);

        // 모델에 데이터 추가
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("result", result);

        // 반환할 JSP 페이지 경로 (mate/profileDetail.jsp)
        return "mypage/mate/profileDetail"; // 경로는 실제 JSP 위치에 맞게 수정
    }

	@PostMapping(value = "/handleFriendRequest.ajax")
	@ResponseBody
	public Map<String, Object> handleFriendRequest(String unlikeId, String action, HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			response.put("success", false);
			response.put("message", "로그인이 필요합니다.");
			return response;
		}

		try {
			if ("accept".equals(action)) {
				// 수락 처리 로직
				mypageService.acceptFriendRequest(id, unlikeId);
			} else if ("reject".equals(action)) {
				// 거절 처리 로직
				mypageService.rejectFriendRequest(id, unlikeId);
			}
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "처리 중 오류가 발생했습니다.");
			logger.error("Error handling friend request: {}", e.getMessage());
		}

		return response;
	}

	@GetMapping(value = "/requestingMateListView")
	public String requestingMateListView(HttpSession session, Model model) {
		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myMateListView");
		return "mypage/requestingMateList";
	}

	@GetMapping(value = "/appliedMates.ajax")
	@ResponseBody
	public Map<String, Object> getAppliedMates(String page, String cnt, HttpSession session, Model model) {
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다.");
			return null;
		}

		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		Map<String, Object> result = mypageService.getAppliedMates(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@PostMapping(value = "/cancelMateApplication.ajax")
	@ResponseBody
	public Map<String, Object> cancelMateApplication(String unlikeId, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		String id = (String) session.getAttribute("loginId");

		// ID가 null인 경우 처리
		if (id == null) {
			response.put("success", false);
			response.put("message", "로그인이 필요합니다.");
			return response;
		}

		mypageService.cancelMateApplication(id, unlikeId);
		response.put("success", true);

		return response;
	}

	@GetMapping(value = "/myIconListView")
	public String myIconListView(HttpSession session, Model model) {
		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myIconListView");
		return "mypage/myIconList";
	}

	@GetMapping(value = "/myIconList.ajax")
	@ResponseBody // JSON 형식으로 응답
	public Map<String, Object> myIconList(String page, String cnt, HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // 에러 응답 반환
		}

		// 친구 요청 가져오기 (ID를 매개변수로 전달)
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.myIconList(page_, cnt_, id);
	    String selectedIconId = mypageService.getSelectedIcon(id);  // 사용자가 설정한 아이콘 ID
	    result.put("selectedIcon", selectedIconId);
		logger.info("Result from service: {}", result);

		return result;
	}
	
	@PostMapping(value = "/iconImageUpdate.ajax")
	@ResponseBody
	public Map<String, Object> iconImageUpdate(@RequestParam("iconId") String iconId, HttpSession session, Model model) {
	    String id = (String) session.getAttribute("loginId");

	    Map<String, Object> response = new HashMap<>();

	    if (id == null) {
	        response.put("error", "로그인이 필요합니다.");
	        return response; // 로그인되지 않은 경우 에러 메시지 반환
	    }

	    // 아이콘 ID가 null 또는 "null" 문자열인 경우, 기본 아이콘으로 설정
	    if (iconId == null || iconId.trim().isEmpty() || "null".equals(iconId)) {
	        boolean updateSuccess = mypageService.iconImageUpdateToDefault(id);  // 기본 아이콘으로 업데이트하는 서비스 호출
	        if (updateSuccess) {
	            response.put("success", true);  // 기본 아이콘으로 변경 성공
	        } else {
	            response.put("error", "기본 아이콘 변경 실패");
	        }
	    } else {
	        // 실제 아이콘 ID로 변경
	        Long iconIdLong = Long.parseLong(iconId);
	        boolean updateSuccess = mypageService.iconImageUpdate(iconIdLong, id);
	        if (updateSuccess) {
	            response.put("success", true);  // 아이콘 이미지 업데이트 성공
	        } else {
	            response.put("error", "아이콘 이미지 변경 실패");
	        }
	    }

	    return response;  // 결과를 JSON 형태로 반환
	}

	@GetMapping(value = "/myBoardListView")
	public String myBoardListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myBoardListView");
		return "mypage/myBoardList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/myBoardListView.ajax")
	@ResponseBody
	public Map<String, Object> myBoardList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.myBoardList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}
	
	@RequestMapping(value="/boardDetail/{boardType}/{board_idx}")
	public String detail(@PathVariable String boardType, @PathVariable int board_idx) {
	    // boardType (게시판 종류)에 따라 이동할 URL을 결정하고, 해당 게시판의 상세보기 페이지로 리다이렉트

	    if ("B100".equals(boardType)) {
	        // 자유게시판 게시글 상세보기 페이지로 이동
	    	 return "redirect:/runBoardDetail/" + board_idx;
	    } else if ("B101".equals(boardType)) {
	        // 런닝코스 게시글 상세보기 페이지로 이동
	    	return "redirect:/freeBoardDetail/" + board_idx;
	    }

	    // 예외 처리: 잘못된 boardType이 들어왔을 경우 (예: 'free' 또는 'run' 외의 값)
	    return "error"; // 오류 페이지로 리턴
	}

	@GetMapping(value = "/myCommentListView")
	public String myCommentListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "myBoardListView");
		return "mypage/myCommentList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/myCommentList.ajax")
	@ResponseBody
	public Map<String, Object> myCommentList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.myCommentList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/likedBoardListView")
	public String likedBoardListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "likedBoardListView");
		return "mypage/likedBoardList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/likedBoardList.ajax")
	@ResponseBody
	public Map<String, Object> likedBoardList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.likedBoardList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/messageListView")
	public String messageListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "messageListView");
		return "mypage/messageList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/messageList.ajax")
	@ResponseBody
	public Map<String, Object> messageList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> params = new HashMap<>();
		params.put("page", page_);
		params.put("cnt", cnt_);
		params.put("id", id);

		// 서비스 호출
		Map<String, Object> result = mypageService.messageList(params);
		logger.info("Result from service: {}", result);
		return result;
	}
	
	@GetMapping(value = "/crewMasterMessageListView")
	public String crewMasterMessageListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "messageListView");
		return "mypage/crewMasterMessageList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/crewMasterMessageList.ajax")
	@ResponseBody
	public Map<String, Object> crewMasterMessageList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);
			
		Map<String, Object> params = new HashMap<>();
		params.put("page", page_);
		params.put("cnt", cnt_);
		params.put("id", id);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.crewMasterMessageList(params);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/memberCrewListView")
	public String memberCrewListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "memberCrewListView");
		return "mypage/memberCrewList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/memberCrewList.ajax")
	@ResponseBody
	public Map<String, Object> memberCrewList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.memberCrewList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/requestedCrewListView")
	public String requestedCrewListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "memberCrewListView");
		return "mypage/requestedCrewList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/requestedCrewList.ajax")
	@ResponseBody
	public Map<String, Object> requestedCrewList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.requestedCrewList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/likedCrewListView")
	public String likedCrewListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "memberCrewListView");
		return "mypage/likedCrewList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/likedCrewList.ajax")
	@ResponseBody
	public Map<String, Object> likedCrewList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.likedCrewList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/likedMemberListView")
	public String likedMemberListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "likedMemberListView");
		return "mypage/likedMemberList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/likedMemberList.ajax")
	@ResponseBody
	public Map<String, Object> likedMemberList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.likedMemberList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

	@GetMapping(value = "/blockMemberListView")
	public String blockMemberListView(HttpSession session, Model model) {

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return "member/login"; // 로그인 페이지로 리다이렉트
		}
		MemberDTO member = mypageService.profileDetail(id);
		ProfileDTO profile = mypageService.ProfileImage(id);
		model.addAttribute("profile", profile);
		model.addAttribute("member", member);
		model.addAttribute("pageName", "likedMemberListView");
		return "mypage/blockMemberList"; // 로그인된 경우 리스트 페이지로 이동
	}

	@GetMapping(value = "/blockMemberList.ajax")
	@ResponseBody
	public Map<String, Object> blockMemberList(String page, String cnt, HttpSession session, Model model) {
		logger.info("list called with page: {}, cnt: {}", page, cnt);

		// 세션에서 사용자 ID를 가져오기
		String id = (String) session.getAttribute("loginId");
		if (id == null) {
			logger.warn("User not logged in. Redirecting to login page.");
			model.addAttribute("msg", "로그인이 필요합니다."); // 모델에 메시지 추가
			return null; // AJAX 요청에 대한 적절한 응답을 반환
		}

		// 페이지와 항목 수 변환
		int page_ = Integer.parseInt(page);
		int cnt_ = Integer.parseInt(cnt);

		// 서비스 호출 시 ID 전달
		Map<String, Object> result = mypageService.blockMemberList(page_, cnt_, id);
		logger.info("Result from service: {}", result);

		return result;
	}

}
