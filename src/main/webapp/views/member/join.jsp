<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/common.css">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"></script>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	display: block; /* flex 대신 block으로 설정 */
	justify-content: center;
	align-items: center;
	height: 140vh;
	background-color: #ffffff;
}

.container {
	background: white;
	padding: 3 0px;
	border-radius: 8px;
	width: 500px;
	margin-top: 150px; /* 헤더 높이만큼 아래로 이동 */
	margin-bottom: 100px; /* 헤더 높이만큼 아래로 이동 */
	border: none; /* 테두리 추가 */
}

h3 {
	text-align: center;
	margin-bottom: 20px;
	font-size: 24px; /* 폰트 크기 증가 */
	font-weight: bold; /* 볼드 처리 */
}

.form-group {
	margin-bottom: 15px;
	display: flex;
	flex-direction: column; /* 세로로 배치 */
}

.input-group {
	display: flex; /* Flexbox로 수평 정렬 */
	align-items: center; /* 수직 중앙 정렬 */
}

label {
	margin-bottom: 5px; /* 레이블과 입력칸 사이의 간격 조정 */
	font-weight: bold;
	font-size: 16px; /* 폰트 크기 증가 */
}

input[type="text"], input[type="password"], input[type="email"], input[type="number"]
	{
	flex: 1; /* 입력칸이 남은 공간을 차지하도록 설정 */
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
	width: calc(101% - 5px); /* 여기서 30px 값을 더 줄이거나 조정 */
}

button {
	background-color: #ff7f50;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	padding: 10px;
	margin-left: 5px; /* 입력 칸과 버튼 사이의 간격 */
}

button:hover {
	background-color: #ff6347;
}

.result {
	margin-left: 10px; /* 버튼과 결과 사이의 간격 */
	font-weight: bold;
	display: inline-block; /* 수평 정렬 */
}

.submit-group {
	text-align: right; /* 버튼을 오른쪽으로 정렬 */
}

.gender-group {
	display: flex; /* 수평 정렬을 위해 Flexbox 사용 */
	align-items: center; /* 수직 중앙 정렬 */
	gap: 20px; /* 남과 여 사이에 여유 공간 추가 */
}

.submit-button {
	position: relative; /* 버튼 위치를 조정할 수 있도록 설정 */
	width: 100%; /* 버튼을 전체 너비로 설정 */
	top: 0; /* 필요 시 조정 */
	right: 5; /* 필요 시 조정 */
}
</style>
</head>
<body>
	<jsp:include page="../header.jsp" />
	<div class="container">
		<h3>회원가입</h3>
		<form id="joinForm" action="join" method="post">
			<div class="form-group">
				<label for="id">아이디 *</label>
				<div class="input-group">
					<input type="text" name="id" required placeholder="예시) runrunrun" />
					<button type="button" id="idCheck">중복확인</button>
				</div>
				<span id="idResult" class="result"></span>
			</div>
			<div class="form-group">
				<label for="pw">비밀번호 *</label> <input type="password" name="pw" required placeholder="특수문자, 영문, 숫자 8~16자" />
			</div>
			<div class="form-group">
				<label for="pwConfirm">비밀번호 확인 *</label> <input type="password" name="pwConfirm" required />
			</div>
			<div class="form-group">
				<label for="name">이름 *</label> <input type="text" name="name" required />
			</div>
			<div class="form-group">
				<label for="nickName">닉네임 *</label>
				<div class="input-group">
					<input type="text" name="nickName" required placeholder="예시) 달려달려달려" />
					<button type="button" id="nickNameCheck">중복확인</button>
				</div>
				<span id="nickNameResult" class="result"></span>
			</div>
			<div class="form-group">
				<label for="address">주소 *</label> <input type="text" name="address" required placeholder="예시) 서울시 금천구 가산동" /> <!-- 주소 api가지고 와서 클릭하게 -->
			</div>
			<div class="form-group">
				<label>성별 *</label>
				<div class="gender-group">
					<input type="radio" name="gender" value="남" required /> 남 
					<input type="radio" name="gender" value="여" required /> 여
				</div>
			</div>
			<div class="form-group">
				<label for="birth">생년월일 *</label> <input type="number" name="birth" required placeholder="생년월일 8자리 입력" /> <!-- 캘린더나 자바제공 달력을 써야 하나? -->
			</div>
			<div class="form-group">
			    <label for="email">이메일 *</label>
			    <div class="input-group">
			        <input type="email" name="email" id="email" required placeholder="예시) run@naver.com" />
			        <button type="button" id="emailCheck">이메일 확인</button>
			    </div>
			    <span id="emailResult" class="result"></span>
			
			    <!-- 이메일 인증번호 입력란 -->
			    <div id="emailVerification" style="display:none;">
			        <label for="verificationCode">인증번호 *</label>
			        <div class="input-group">
			            <input type="text" id="verificationCode" name="verificationCode" placeholder="인증번호 입력" />
			            <button type="button" id="verifyCodeBtn">인증번호 확인</button>
			        </div>
			        <span id="verificationResult" class="result"></span>
			    </div>
			</div>
			<div class="form-group">
				<label for="phoneNumber">휴대전화번호</label> <input type="text" name="phoneNumber" placeholder="예시) 010-1234-5678" />
			</div>
			<div class="submit-group">
				<button type="submit" class="submit-button">가입하기</button>
			</div>
		</form>
	</div>
	<jsp:include page="../footer.jsp" />
</body>

<script>
    var msg = '${msg}';
    if (msg != '') {
        alert(msg);
    }

    // 중복 체크 상태 변수
    var isIdChecked = false;
    var isNickNameChecked = false;
    var isEmailChecked = false;

    // ID 중복 체크
    $('#idCheck').click(function() {
        var id = $('input[name="id"]').val().trim();
        
        // 입력값이 비어있는지 체크
        if (id === '') {
            alert('아이디를 먼저 입력해주세요.');
            return;
        }

        // 한글과 특수문자를 모두 포함한 정규식
        var idPattern = /[\u1100-\u11FF\u3130-\u318F\uAC00-\uD7A3]/;  // 한글 자모와 완성형 한글
        var specialCharPattern = /[^a-zA-Z0-9]/; // 특수문자 포함 여부 체크 (영문, 숫자 외)

        // 한글이 포함된 경우
        if (idPattern.test(id)) {
            $('#idResult').html('아이디에 한글 사용은 불가합니다.').css('color', 'red');
            isIdChecked = false;  // 중복 확인 상태 리셋
            return;
        }

        // 특수문자가 포함된 경우
        if (specialCharPattern.test(id)) {
            $('#idResult').html('아이디에 특수문자 사용은 불가합니다.').css('color', 'red');
            isIdChecked = false;  // 중복 확인 상태 리셋
            return;
        }

        $.ajax({
            type: 'get',
            url: 'idOverlay',
            data: { 'id': id },
            dataType: 'JSON',
            success: function(data) {
                if (data.overlay > 0) {
                    $('#idResult').html(id + ' 는 이미 사용중 입니다.').css('color', 'red');
                    isIdChecked = false;  // 중복 확인 실패 상태
                } else {
                    $('#idResult').html(id + ' 는 사용 가능합니다.').css('color', 'green');
                    isIdChecked = true;  // 중복 확인 성공 상태
                }
            },
            error: function(e) {
                console.log(e);
            }
        });
    });

    // 닉네임 중복 체크
    $('#nickNameCheck').click(function() {
        var nickName = $('input[name="nickName"]').val().trim();

        // 입력값이 비어있는지 체크
        if (nickName === '') {
            alert('닉네임을 먼저 입력해주세요.');
            return;
        }
        
        // 특수문자 포함 여부 체크
        var specialCharPattern = /[^a-zA-Z0-9가-힣]/; // 특수문자 포함 여부 체크 (영문, 숫자, 한글 외)
        if (specialCharPattern.test(nickName)) {
            $('#nickNameResult').html('닉네임에 특수문자는 사용할 수 없습니다.').css('color', 'red');
            isNickNameChecked = false;  // 중복 확인 상태 리셋
            return;
        }

        $.ajax({
            type: 'get',
            url: 'nickNameOverlay',
            data: { 'nickName': nickName },
            dataType: 'JSON',
            success: function(data) {
                if (data.overlay > 0) {
                    $('#nickNameResult').html(nickName + ' 는 이미 사용중 입니다.').css('color', 'red');
                    isNickNameChecked = false;  // 중복 확인 실패 상태
                } else {
                    $('#nickNameResult').html(nickName + ' 는 사용 가능합니다.').css('color', 'green');
                    isNickNameChecked = true;  // 중복 확인 성공 상태
                }
            },
            error: function(e) {
                console.log(e);
            }
        });
    });

    // 이메일 중복 체크
    $('#emailCheck').click(function() {
        var email = $('input[name="email"]').val().trim();

     // 이메일 형식 체크
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email === '') {
            $('#emailResult').html('이메일을 먼저 입력해주세요.').css('color', 'red');
            return;
        }

        if (!emailPattern.test(email)) {
            $('#emailResult').html('유효한 이메일 주소를 입력해주세요.').css('color', 'red');
            return;
        }

        $.ajax({
            type: 'get',
            url: 'emailOverlay',
            data: { 'email': email },
            dataType: 'JSON',
            success: function(data) {
                if (data.overlay > 0) {
                    $('#emailResult').html(email + ' 는 이미 사용중 입니다.').css('color', 'red');
                    isEmailChecked = false;  // 중복 확인 실패 상태
                } else {
					// 이메일 중복이 없는 경우 인증번호 발송
					$.ajax({
						type: 'post',
						url: 'sendVerificationCode',
						data: {'email' : email},
						dataType: 'JSON',
						success: function(data) {
							if (data.success) {
								$('emailResult').html('인증번호가 발송되었습니다.').css('color', 'green');
								$('emailVerification').show(); // 인증번호 보이게 함.
							} else {
								$('emailResult').html('이메일 전송에 실패했습니다.').css('color', 'red');
							}
						},
						error: function(e) {
							console.log(e);
						}
					});               	
                }
            },
            error: function(e) {
                console.log(e);
            }
        });
    });

    // 입력 시 공백 제거
    $('input').on('input', function() {
        var value = $(this).val();
        $(this).val(value.replace(/\s+/g, '')); // 공백 제거
    });

    // 유효성 검사 및 중복 체크 상태 확인
    $('#joinForm').on('submit', function(event) {
        var valid = true;
        var id = $('input[name="id"]').val().trim();
        var pw = $('input[name="pw"]').val().trim();
        var pwConfirm = $('input[name="pwConfirm"]').val().trim();
        var nickName = $('input[name="nickName"]').val().trim();
        var address = $('input[name="address"]').val().trim();
        var birth = $('input[name="birth"]').val();
        var email = $('input[name="email"]').val().trim();
        var phoneNumber = $('input[name="phoneNumber"]').val().trim();

        // 중복 체크 상태가 모두 true여야만 진행
        if (!isIdChecked) {
            alert('아이디 중복 확인을 해주세요.');
            valid = false;
        } else if (!isNickNameChecked) {
            alert('닉네임 중복 확인을 해주세요.');
            valid = false;
        } else if (!isEmailChecked) {
            alert('이메일 중복 확인을 해주세요.');
            valid = false;
        }

        // 입력값이 비어있으면 제출을 방지
        if (id === '' || pw === '' || pwConfirm === '' || nickName === '' || address === '' || birth === '' || email === '') {
            alert('모든 필드를 채워주세요.');
            valid = false;
        }

        // ID 유효성 검사
        var idPattern = /^[a-zA-Z0-9]+$/;
        if (id.length < 4 || id.length > 20 || !idPattern.test(id)) {
            if (id.length < 4 || id.length > 20) {
                alert('ID는 4자리 이상 20자리 이하이어야 합니다.');
            } else if (!idPattern.test(id)) {
                alert('ID는 영어와 숫자만 포함할 수 있습니다.');
            }
            valid = false;
        } else if (valid) { // ID가 유효한 경우에만 진행
            // 비밀번호 유효성 검사
            var passwordPattern = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\W_]).{8,16}$/;
            if (!passwordPattern.test(pw)) {
                alert('비밀번호는 숫자, 영문, 특수문자를 포함하여 8자 이상 16자 이하이어야 합니다.');
                valid = false;
            }
        }

        // 비밀번호 확인
        if (valid && pw !== pwConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            valid = false;
        }

        // 닉네임 유효성 검사
        if (valid) {
            var nickNamePattern = /^[a-zA-Z0-9가-힣]+$/;
            if (!nickNamePattern.test(nickName)) {
                alert('닉네임은 영문, 숫자, 한글만 포함할 수 있습니다.');
                valid = false;
            }
        }

        // 생년월일 유효성 검사
        if (valid) {
            var birthPattern = /^\d{8}$/;
            if (!birthPattern.test(birth)) {
                alert('생년월일은 8자리 숫자로 입력해야 합니다.');
                valid = false;
            }
        }

        // 이메일 유효성 검사
        if (valid) {
            var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                alert('유효한 이메일 주소를 입력하세요.');
                valid = false;
            }
        }

        if (valid && phoneNumber !== '') {
            var phonePattern = /^010-\d{4}-\d{4}$/;
            if (!phonePattern.test(phoneNumber)) {
                alert('유효한 전화번호(010-1234-5678)을 입력하세요.');
                valid = false;
            }
        }

        // 유효하지 않으면 폼 제출 중단
        if (!valid) {
            event.preventDefault();
        }
    });
</script>
</html>