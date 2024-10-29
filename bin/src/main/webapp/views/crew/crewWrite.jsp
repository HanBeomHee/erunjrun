<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>crewWrite</title>
	<link rel="stylesheet" href="/resources/css/crew.css">
	<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
	
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script src="/resources/js/summernote.js"></script>


    <style>
        #img_miri {
            width: 300px;          /* 박스의 너비 설정 */
            height: 200px;         /* 박스의 높이 설정 */
            border: 1px solid #EAEAEA;  /* 연한 회색(#d3d3d3) 선 설정 */
            background-color: transparent;  /* 배경색 없음 */
        }

        #file {
            display: none;
        }

        #dori {
            width: 1280px;          /* 박스의 너비 설정 */
            /* 박스의 높이 설정 */
            border: 1px solid #EAEAEA;  /* 연한 회색(#d3d3d3) 선 설정 */
            background-color: transparent;
            margin: 80px auto 0;
        }

        .content_layout {
            padding: 20px;
        }

    </style>
</head>
<body>
    <a href="/crewUpdateView">크루 업데이트 페이지로 이동</a>
    <jsp:include page="../header.jsp" />
    
    
    <div class="crewWriteView"> <!-- 체크박스 순위 올리기 위함 -->

        <div class="inner">
            <form enctype="multipart/form-data">
                <p class="title1">러닝크루 등록</p>

                <div id="dori">
                    <div class="twobox">
                        <div id="img_miri"></div>
                        <span style='color:#d3d3d3'>※ 권장 사이즈 800px / 400px</span>
                        <input type="file" name="crew_img" onchange="readFile(this)" id="file" />
                        <label for="file">
                            <div class="btn03-m">이미지 업로드</div>
                        </label>
                    </div>
                    <div class="firstbox"> <!-- 레이아웃 구성을 위한 div -->

                        <div class="boxheigth">
                            <span class="title2">크루명 </span>
                            <input type="text" name="crew_name" required />
                        </div> <br>

                        <span class="title2">태그</span>
                        <span id="tagFilters">
                            <label><input type="checkbox" name="tag_idx_list" value="1">🏃‍♂️러닝에 집중</label>
                            <label><input type="checkbox" name="tag_idx_list" value="2">🙋‍♀️ 친목도 중요</label>
                            <label><input type="checkbox" name="tag_idx_list" value="3"> 남성만 가능</label>
                            <label><input type="checkbox" name="tag_idx_list" value="4"> 여성만 가능</label>
                            <label><input type="checkbox" name="tag_idx_list" value="5"> 혼성</label>
                            <label><input type="checkbox" name="tag_idx_list" value="6"> <b style='color: FD6F22'>E</b> 환영해요</label>
                            <label><input type="checkbox" name="tag_idx_list" value="7"> <b style='color: 116DCA'>I</b> 환영해요</label>
                            <label><input type="checkbox" name="tag_idx_list" value="8">🐂 소규모 크루</label>
                            <label><input type="checkbox" name="tag_idx_list" value="9">🏆 대회 목적</label>
                            <label><input type="checkbox" name="tag_idx_list" value="10">💦 러닝 고수만</label>
                            <label><input type="checkbox" name="tag_idx_list" value="11">🥳 초보도 환영</label>
                        </span> <br>

                        <div class="boxheigth">
                            <span class="title2">요일</span>
                            <input type="checkbox" name="days" value="mon" /><span class="basictex">월</span>
                            <input type="checkbox" name="days" value="tue" /><span class="basictex">화</span>
                            <input type="checkbox" name="days" value="wen" /><span class="basictex">수</span>
                            <input type="checkbox" name="days" value="thu" /><span class="basictex">목</span>
                            <input type="checkbox" name="days" value="fri" /><span class="basictex">금</span>
                            <input type="checkbox" name="days" value="sat" /><span class="basictex">토</span>
                            <input type="checkbox" name="days" value="sun" /><span class="basictex">일</span>
                        </div> <br>

                        <div class="boxheigth">
                            <span class="title2">인원</span> 
                            <input type="number" name="member" min="2" required />
                        </div> <br>

                        <div class="boxheigth">
                            <span class="title2">운동강도</span>
                            <input type="number" name="minute" min="1" required /><span class="basictex"> / </span>
                            <input type="number" name="distance" min="1" required /><span class="basictex"> km </span>
                        </div> <br>

                        <div class="boxheigth">
                            <span class="title2">지역</span>
                            <input type="text" name="address" id="sample4_roadAddress" required disabled />
                            <input type="button" onclick="search()" class="btn02-m" value="검색">
                        </div>

                    </div> <!-- 레이아웃 구성을 위한 div --> <br>

                    <div class="content_layout"> <!-- 레이아웃 구성을 위한 div -->
                        <p class="title2">크루 설명</p> <br><br>
                        <div class="post-form">
                            <textarea name="postContent" id="summernote" maxlength="10000"></textarea>
                        </div>
                    </div>

                </div>

                <div class="btn-parent">
                    <button type="button" class="btn03-l">등록 취소하기</button>
                    <button type="button" class="btn01-l" onclick="submitPost()">크루 등록하기</button>
                </div>
            </form>
        </div>
    </div>
    <jsp:include page="../footer.jsp" />
</body>

<script src="/resources/js/common.js"></script>
<script src="/resources/js/daumapi.js"></script>

<script>

	// 크루 대표 이미지 미리보기
    function readFile(input) {
        var reader;
        $('#img_miri').empty();

        for (var file of input.files) {
            reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function (e) {
                $('#img_miri').append('<img class="preview" width="300px" height="200px" src="' + e.target.result + '"/>');
            }
        }
    }

    var dayCheckboxes = [];  // 선택된 요일 체크박스를 추적할 배열

    // 모든 체크박스에 change 이벤트 리스너 추가
    $('input[name="days"]').on('change', function () {
        if ($(this).is(':checked')) {
            if (dayCheckboxes.length >= 2) {
                var firstChecked = dayCheckboxes.shift();
                $(firstChecked).prop('checked', false);
            }
            dayCheckboxes.push(this);
        } else {
            dayCheckboxes = dayCheckboxes.filter(item => item !== this);
        }
    });
    var tagCheckboxes = [];  // 선택된 태그 체크박스를 추적할 배열

    // 모든 체크박스에 change 이벤트 리스너 추가
    $('input[name="tag_idx_list"]').on('change', function () {
        if ($(this).is(':checked')) {
            if (tagCheckboxes.length >= 3) {
                var firstChecked = tagCheckboxes.shift(); // 배열에서 첫 번째 체크박스를 제거
                $(firstChecked).prop('checked', false); // 첫 번째 체크박스 해제
                $(firstChecked).parent().removeClass('checked'); // 해당 체크박스의 label에서 'checked' 클래스 제거
            }
            tagCheckboxes.push(this); // 새로운 체크박스를 배열에 추가
            $(this).parent().addClass('checked'); // 현재 체크박스의 label에 'checked' 클래스 추가
        } else {
            tagCheckboxes = tagCheckboxes.filter(item => item !== this); // 선택 해제된 체크박스를 배열에서 제거
            $(this).parent().removeClass('checked'); // label에서 'checked' 클래스 제거
        }
    });

    function submitPost() {
        // formData 생성
        var formData = new FormData($('form')[0]);

        var content = $('#summernote').summernote('code');

        var fileInput = $('input[type="file"]')[0]; // 파일 input에서 파일 가져오기
        if (fileInput.files.length > 0) {
            formData.append('crew_img', fileInput.files[0]); // 파일 데이터 추가
            console.log(fileInput);
        }

        formData.append('id', 'test'); // 세션값 체크해서 넣어줘야 함!
        formData.append('content', content);  // summernote의 HTML 내용 추가 (이미지 포함)

        formData.append('address', roadAddr); // 화면에 출력
        formData.append('sigungu', sigungu);
        formData.append('sido', sido);
        formData.append('shortsido', shortsido);

        console.log('tagCheckboxes=>', tagCheckboxes);
        console.log('roadAddr =>', roadAddr);
        console.log('sigungu =>', sigungu);
        console.log('sido =>', sido);
        console.log('shotssido =>', shortsido);

        var selectedTags = "";
        $('input[name="tag_idx_list"]:checked').each(function () {
            selectedTags += $(this).val() + ",";
        });

        selectedTags = selectedTags.slice(0, -1);

        // tag_idx_list 다시 set
        formData.set('tag_idx_list', selectedTags);

        // 게시글 에디터 이미지 검증을 위한 코드
        var tempDom = $('<div>').html(content);
        var imgsInEditor = [];

        // 에디터의 이미지 태그에서 new_filename을 추출해 배열에 추가
        tempDom.find('img').each(function () {
            var src = $(this).attr('src');
            if (src && src.includes('/photo-temp/')) {  // 경로 검증을 위해 추가
                var filename = src.split('/').pop();  // 파일명만 추출
                imgsInEditor.push(filename);  // 에디터에 있는 이미지의 new_filename 추출
            }
        });

        // new_filename과 일치하는 항목만 필터링
        var finalImgs = tempImg.filter(function (temp) {
            return imgsInEditor.includes(temp.img_new);  // 에디터에 있는 파일과 tempImg의 new_filename 비교
        });

        console.log("최종 전송할 이미지 쌍:", finalImgs);

        // 최종 이미지 파일명 배열을 JSON으로 변환하여 추가
        formData.append('imgsJson', JSON.stringify(finalImgs));  // new_filename과 일치하는 값만 전

        $.ajax({
            type: 'POST',
            url: '/crew/write',  // 서버에 전송할 URL
            data: formData,  // formData 객체 전송
            contentType: false,  // formData 사용 시 false로 설정
            processData: false,  // formData 사용 시 false로 설정
            enctype: 'multipart/form-data',  // multipart/form-data 사용
            success: function (response) {
                console.log('글 전송 성공:', response);
                if(response.success){
                	console.log(response.page);
                 	/* location.href=response.page;  */
                }
            },
            error: function (e) {
                console.log('글 전송 에러:', e);
            }
        });
    }
    
    

    
    
    
    
</script>

</html>