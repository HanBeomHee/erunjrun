<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<title>Crew Member List</title>
<link rel="stylesheet" href="/resources/css/common.css">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"></script>
    <script src="/resources/js/jquery.twbsPagination.js" type="text/javascript"></script>
    <script src="/resources/js/layerPopup.js"></script>
<style>
	a{
		color: #333;
	}
	#searchForm {
        float: right;
        margin: 10px;
    }
    .btn01-l{
    	float: right;
    	margin: -80px -80px;	
    }
	.container{
		margin-top: 18px;
	}
	.highlight {
    	background-color: FFEADE;
    	
    }
    .tag-l-bd{
    	margin-left: 51px;
    	background-color: #FB7E3A;
    	color: #fff;
    }
    .title1{
    	margin-top: 160px;
    }
    
    /*운동프로필 레이어팝업*/
	#profilePopup {
	    width: fit-content;
	    top: 185px;
	    left: 50%;
	    transform: translateX(-50%);
		z-index: 996;
	}
	#profilePopup .close {
	    font-size: 40px;
	    font-weight: 300;
	    position: absolute;
	    z-index: 996;
	    top: 30px;
	    right: 30px;
	    display: inline-block;
	    width: 30px;
	    height: 30px;
	    line-height: 27px;
	    text-align: center;
	}
	#profilePopup .modal-content{
	    padding: 20px 20px;
	    background: #f8f8f8;
	    border: none;
    	border-radius: 0;
    }
    #PopupBody {
    	background: #fff;
    	border-radius: 10px;
    }
	   .profileDetail {
   	   position: relative;
   	   width: 760px;
       padding: 34px 50px 10px;
       height: fit-content !important;
   	   overflow-y: auto;
   }
    .profileDetail .user-info .user-name{
       font-size: 30px;
       margin-bottom: 0px;
   }
   .addr{
   	    transform: translateY(5px);
    	display: inline-block;
   }
   
   .layoutbox-bt {
	    height: 90px; /* 하단 배너 영역의 높이를 90px로 설정 */
	    background-color: white; /* 배경색을 흰색으로 설정 */
	}
    
    .input-txt-l01{
    	width: 300px;
   	 	padding: 11px 8px;
    	height: 46px;
    }
    
    .genderImg{
    	padding-bottom: 2px;
    }
    
    #btn04-s{
    	display: inline-block;
        height: 32px;
        padding: 6px 11px;
        margin: 0 4px;
        border-radius: 10px;
        border: 1px solid #6994FF;
        color: #6994FF;
        background: #fff;
        cursor: pointer;
        font-size: 14px;
    }
    
     .profileBox{
    	margin-right: 10px;
    } 

	
	/* 프로필 컨테이너 */
	table tbody tr td.profileContainer {
	    display: block;
	    align-items: center;
	    vertical-align: middle;
	    text-align: left;
    	margin-left: 60px;
	}   
</style>
</head>
<body>
	<jsp:include page="../header.jsp"/>
	
	<div class="inner">
	<p class="title1">크루 신청자 관리</p>
	<form id="searchForm">
    <select id="searchOption">
        <option value="subject">닉네임</option>
    </select>
    <input class="input-txt-l01" type="text" id="searchKeyword" placeholder="검색어를 입력하세요"/>
    <input class="btn-sch" type="submit" value="검색"/>
</form>
	<table>
		<thead>
			<tr>
				<th>신청회원</th>
				<th>연령대</th>
				<th>성별</th>
				<th>신청일자</th>
				<th>신청처리</th>
			</tr>
		</thead>
		<tbody id="list" >
	
		</tbody>
		<tr>
			<th colspan="5">
				<div class="container">
		    		<nav aria-label="Page navigation">
		        		<ul class="pagination" id="pagination"></ul>
		    		</nav>
				</div>
			</th>
		</tr>
	</table>
	
	<!-- 모달 -->
		<div id="profilePopup" class="modal">
		    <div class="modal-content">
		        <span class="close">&times;</span>
		        <div id="PopupBody"></div>
		    </div>
		</div>
	</div>
	
	<div class="layoutbox-bt"></div>
	
	<jsp:include page="../footer.jsp"/>
</body>
<script>
	var firstPage = 1;
	var paginationInitialized = false;
	
	pageCall(firstPage);
	
	// 검색 폼 제출 시 AJAX 호출
	$('#searchForm').on('submit', function(event) {
	    event.preventDefault();  // 폼 제출 기본 동작 중지
	    firstPage = 1;
	    paginationInitialized = false;
	    pageCall(firstPage);  // 검색어가 추가된 상태에서 호출
	});
	

	function pageCall(page) {
	    var keyword = $('#searchKeyword').val();  // 검색어
	    var crew_idx = 52; // 나중에 변경 필요
	
	    $.ajax({
	        type: 'POST',
	        url: '/crew/applicationMemberList',
	        data: {
	        	'crew_idx' : crew_idx,
	            'page': page,
	            'cnt': 15,
	            'keyword': keyword  // 검색어
	        },
	        datatype: 'JSON',
	        success: function(response) {
	            console.log(response.result);
	            
	            console.log(response.result.totalpage);
	            
	            var result = response.result;
	            
	            
	            var totalCount = result.length;  // 총 게시글 수를 서버에서 가져옴
	            var pageSize = 15;  // 한 페이지당 게시글 수
	            var totalPages = Math.ceil(totalCount / pageSize);  // 총 페이지 수 계산
	            console.log('총 페이지 수=> ', totalPages);
	            
	            
	            drawList(result);
	            
	            if(!paginationInitialized || keyword !== ''){
	            	$('#pagination').twbsPagination('destroy');
		            $('#pagination').twbsPagination({
		                startPage: page,
		                totalPages: totalPages,
		                visiblePages: 10,
		                initiateStartPageClick: false,
		                onPageClick: function(evt, page) {
		                    console.log('evt', evt);
		                    console.log('page', page);
		                    pageCall(page);
		                }
		            });
		            paginationInitialized = true;
	            }
	        },
	        error: function(e) {
	            console.log(e);
	        }
	    });
	}
	// 게시글 리스트
	function drawList(result) {
		var content ='';
		result.forEach(function(item,idx){
			
			if(item.gender === '남'){
        		genderImg = '<img src="resources/img/common/ico_male.png" width="9px" class="genderImg"/>';
        	}else{
        		genderImg = '<img src="resources/img/common/ico_female.png" width="9px" class="genderImg"/>';
        	}
			
	        var birth = item.birth;
	        //console.log("Birth value:", birth); // birth 값 확인
	        
	         // birth의 연도만 추출
	        var birthYear = parseInt(birth.split('-')[0], 10);

	        // 현재 연도에서 출생 연도를 빼고 나이대 계산
	        var ageGroup = Math.floor((new Date().getFullYear() - birthYear) / 10) * 10 + "대";

	        
            content += '<tr>';
            // 프로필 + 닉네임 (나중에 연결 필요)
            content +='<td class="profileContainer"><img src="resources/img/common/profile.png" width="32px" class="profileBox"/>'+item.nickname+'</td>';
			
			content +='<td>'+ageGroup+'</td>';
			content +='<td>'+genderImg+ '&nbsp;' +item.gender+'</td>';
			content +='<td>'+item.create_date+'</td>'; // 신청일자
			content += '<td><button class="btn02-s" onclick="layerPopup(\'' + item.nickname + '님을 승인 하시겠습니까?\', \'승인\', \'취소\', function() { memberResult(\'' + item.id + '\', \'' + item.nickname + '\', \'Y\'); }, applBtn2Act)">승인</button>';



		 	content += '<button class="btn02-s" id="btn04-s" onclick="layerPopup(\'' + item.nickname + '님을 거절 하시겠습니까?\', \'거절\', \'취소\', function() { memberResult(\'' + item.id + '\', \'' + item.nickname + '\', \'N\'); }, applBtn2Act)">거절</button></td>'; 
	        content += '</tr>';

		});
		$('#list').html(content);
	}
	
	 	$('#loginPop').on('click',function(){
	 		
	 		var userId = "${sessionScope.loginId}";
	 		
	 		if(!userId){
	 			layerPopup('로그인이 필요한 서비스 입니다.','로그인 페이지','닫기',secondBtn1Act,secondBtn1Act);	
	 		}else{
	 			location.href='runBoardWrite';
	 		}
	 		
	 	});
	 	
	 	
/* 	 // 클릭시 운동프로필 레이어 팝업
		$(document).on('click','.user',function(){
		    var toUserId = $(this).data('id');
		   // console.log('toUserId',toUserId);
		    openProfile(toUserId);
		});
		
		
		// 운동프로필 레이어 팝업 열기
		function openProfile(toUserId){
			var modal = document.getElementById("profilePopup");
		    var PopupBody = document.getElementById("PopupBody");
			
		    // AJAX 요청
		    var xhr = new XMLHttpRequest();
		    xhr.open("GET", "/mate/"+toUserId, true);
		    xhr.onreadystatechange = function() {
		        if (xhr.readyState === 4 && xhr.status === 200) {
		            PopupBody.innerHTML = xhr.responseText; // 응답을 모달에 넣기
		            modal.style.display = "block"; // 모달 열기
		            
		         	// JS 파일을 동적으로 로드
		            var script = document.createElement('script');
		            script.src = '/resources/js/profileDetail.js'; 
		            document.body.appendChild(script);
		        }
		    };
		    xhr.send();
		}
		
		// 팝업 닫기
		document.getElementsByClassName("close")[0].onclick = function() {
		    document.getElementById("profilePopup").style.display = "none";
		};  */
		
		// 팝업 취소
		function applBtn2Act() {
		    removeAlert(); 
		}
		
		
		function memberResult(id, nickname, value) {
		    console.log('ID:', id);
		    console.log('Nickname:', nickname);
		    console.log('value:', value);
		    
			var crew_idx = 52; // 나중에 변경 필요
			var code_name = '';
			
			if(value === 'Y'){
				code_name = 'C101';
			}else{
				code_name = 'C102';
			}

			console.log('code_name : ', code_name);			
			console.log('실행됨>');
			
  			$.ajax({
				type: 'POST',
				url: '/crew/applicationWrite',
				data: {'loginId' : id,
					'crew_idx' : crew_idx,
					'code_name' : code_name},
				dataType: 'JSON',
				success: function(response){
					
					console.log('성공');
					
					if(response.success){
						removeAlert();
						layerPopup(response.msg + '완료되었습니다.', '확인',false,applBtn2Act,applBtn2Act);
						pageCall(firstPage);
					}else{
						removeAlert();
						layerPopup(response.msg + '미완료되었습니다.', '확인',false,applBtn2Act,applBtn2Act);
					}
					
				},error: function(e){
					soncole.log('에러남 => ', e);
				}
			}); 
			 
		    
		}
		
		


</script>
</html>