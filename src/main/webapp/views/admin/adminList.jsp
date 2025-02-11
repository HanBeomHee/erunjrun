<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<title>이런저런</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/common.css">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"></script>
<script src="/resources/js/jquery.twbsPagination.js" type="text/javascript"></script>
<style>
   .input-container {
    display: flex;        /* Flexbox 사용 */
    align-items: center; /* 수직 중앙 정렬 */
    margin-bottom: 20px;
    margin-top: 20px;
   }
   #searchForm{
   margin-top: 20px; 
   margin-bottom: 10px; 
   }
   
   #text{
   
    margin-right: 15px
   }
   body {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    margin: 0;
   }
   /* 콘텐츠와 사이드바 감싸는 래퍼 */
   .content-wrapper {
       display: flex;
       width: 100%;
       margin: 80px 10px; /* 헤더 높이만큼 여백 */
       flex-grow: 1; /* 남은 공간 채우기 */
   }

   /* 사이드바 스타일 */
   .fixed-left {
       width: 300px;
       border-right: 1px solid #ccc;
       padding: 20px;
       position: sticky;
       top: 80px; /* 헤더 아래에 고정 */
       height: calc(100vh - 80px); /* 화면 높이에 맞추기 */
       overflow-y: auto;
   }
   .fixed-left p{
       margin: 15px 0;
       line-height: 1.5;
       font-size: 20px;
   }
   
   #admin_name{
   font-weight: 800;
   font-size: 23px;
   }
   
   .image img {
       width: 35%;  /* 또는 원하는 픽셀 값 */
       height: auto;
       margin-bottom: 20px; /* 비율을 유지 */
      }
   /* 메인 콘텐츠 */
   .main-content {
       flex: 1; /* 남은 공간 채우기 */
       padding: 20px 100px 20px 40px;
       overflow: auto;
   }
   #content{
       width: 750px; /* 너비를 250픽셀로 설정 */
        height: 350px;
        resize: vertical; /* 높이를 50픽셀로 설정 */
        font-size: 20px; /* 글자 크기를 18픽셀로 설정 */
        padding: 10px; /* 내부 여백을 10픽셀로 설정 */
    
    }
   .btn-area {
      text-align: right;
   }
   .btn02-l{
   margin-top: 10px;
   margin-bottom: 10px;
   }
   
</style>
</head>
<body class="all">
   <!-- 헤더 -->
   <jsp:include page="../header.jsp"/> 
   
   <!-- 콘텐츠 영역 (사이드바와 메인 콘텐츠를 감싸는 래퍼) -->
   <div class="content-wrapper">
      <aside class="fixed-left">
            <div class="image">
                <img class="profile-img" src="/resources/img/common/admin_profile.png" alt="관리자 프로필 이미지"/>
            </div>
           <p class="title2" id="admin_name">관리자</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminMember'">회원정보</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminReport'">신고</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminAsk'">문의하기</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminTag'">태그</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminIconListView'">아이콘</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminPopup'">팝업</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminCode'">구분코드</p>
            <p class="title3" style="cursor: pointer;" onclick="location.href='/adminJoin'">회원가입</p>
        </aside>
       <main class="main-content">
         <p class="title1" >관리자정보</p>
         <div class="btn03-l" onclick="location.href='/adminMember'">일반회원</div>
          <div class="btn02-l" onclick="location.href='/admin'">관리자</div>
        
          <form id="searchForm">
             <select id="searchOption">
                 <option value="admin_id">아이디</option>
                 <option value="name">이름</option>
            </select>
         <input class="input-txt-l" type="text"  id="searchKeyword" placeholder="검색어를 입력하세요"/>
             <input class="btn-sch" type="button" onclick="pageCall(1)" value="검색"/>
         </form>
         <table>
         <colgroup>
             <col width="30%"/>
             <col width="30%"/>
             <col width="30%"/>
             <col width="30%"/>
          </colgroup>
         <thead>
            <tr>
               <th>아이디</th>
               <th>관리자명</th>
               <th>가입일자</th>
               <th>삭제</th>
            </tr>
         </thead>
          <tbody id="list">
             
             
          </tbody>
          <tr>
            <th colspan="6">
               <div class="container">
                <nav aria-label="Page navigation">
                 <ul class="pagination" id="pagination"></ul>
                </nav>
               </div>
            </th>
            </tr>
            </table>
         </main>
   </div>
   
   <!-- 푸터 -->
   <div class="fixed_footer">
      <jsp:include page="../footer.jsp"/>
   </div>
</body>



<script>
var show = 1;
var paginationInitialized = false;

$(document).ready(function () {
    pageCall(show);
});

function pageCall(page) {
    var keyword = $('#searchKeyword').val(); // 검색어
    var opt = $('#searchOption').val(); // 검색옵션

    $.ajax({
        type: 'POST',
        url: '/adminList',
        data: {
            'page': page,
            'cnt': 15,
            'opt': opt,
            'keyword': keyword
        },
        dataType: 'JSON',  // 대소문자 오류 수정
        success: function (data) {
            console.log(data);
            drawList(data.list);

            // 페이지네이션 초기화 및 재설정
            if (paginationInitialized) {
                $('#pagination').twbsPagination('destroy'); // 기존 페이지네이션 제거
            }

            if (data.totalPages > 0) { // 검색 결과가 있을 때만 페이지네이션 생성
                $('#pagination').twbsPagination({
                    totalPages: data.totalPages,
                    visiblePages: 10,
                    startPage: page,
                    initiateStartPageClick: false,
                    onPageClick: function (evt, page) {
                        pageCall(page);
                    }
                });
                paginationInitialized = true; // 페이지네이션 초기화 완료 상태로 설정
            } else {
                paginationInitialized = false; // 검색 결과가 없으면 초기화 상태로 설정
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function drawList(list) {
    var content = '';
    for (var view of list) {
        content += '<tr>';
        content += '<td>' + view.admin_id + '</td>';
        content += '<td>' + view.name + '</td>';
        content += '<td>' + view.join_date + '</td>';
        content += '<td><a href="/adminYn/' + view.admin_id + '" style="color: orange;">삭제</a></td>';
        content += '</tr>';
    }
    $('#list').html(content);
}

// 메시지 경고창 띄우기
var msg = '${msg}';
if (msg != '') {
    alert(msg);
}

</script>
<script src="/resources/js/common.js" type="text/javascript"></script>
<script src="/resources/js/layerPopup.js"></script>
</html>