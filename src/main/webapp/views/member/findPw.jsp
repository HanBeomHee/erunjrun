<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/common.css">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"></script>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 20px;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background-color: #ffffff;
}

.container {
	background: white;
	padding: 20px;
	border-radius: 8px;
	width: 400px;
}

h3 {
	text-align: center;
	margin-bottom: 20px;
	font-size: 28px; /* 폰트 크기 키우기 */
	font-weight: bold; /* 볼드 처리 */
}

.form-group {
	margin-bottom: 15px;
}

label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

input[type="text"], input[type="email"] {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

button {
	padding: 10px;
	background-color: #ff7f50; /* 주황색 */
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	width: 100%; /* 버튼 너비 100% */
}

button:hover {
	background-color: #ff6347; /* 버튼 hover 색상 */
}

.info {
    margin: 15px 0; /* 위아래 여백 추가 */
    font-size: 14px;
    color: #555;
    text-align: center; /* 중앙 정렬 */
    background-color: #f0f0f0; /* 회색 배경 */
    padding: 10px; /* 안쪽 여백 추가 */
    border-radius: 4px; /* 모서리 둥글게 */
}
</style>
</head>
<body>
	<jsp:include page="../header.jsp" />
	<div class="container">
		<h3>비밀번호 찾기</h3>
		<div class="info">회원가입 시 입력한 아이디, 이름, 이메일을 입력해주세요.</div>
		<form action="tempPw" method="post">
			<div class="form-group">
				<label for="id">아이디</label> <input type="text" id="id" name="id"
					required />
			</div>
			<div class="form-group">
				<label for="name">이름</label> <input type="text" id="name"
					name="name" required />
			</div>
			<div class="form-group">
				<label for="email">이메일</label> <input type="email" id="email"
					name="email" required />
			</div>
			<button type="submit">확인</button>
		</form>
	</div>
</body>
</html>
