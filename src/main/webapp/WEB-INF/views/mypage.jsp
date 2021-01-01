<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<link  rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
	<link rel="stylesheet" href="assets/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/css/common.css" type="text/css">
	<style type="text/css">
		#changePwForm , #changeNameForm {padding: 50px 10px 10px 10px;}
		.item{  border-radius: 20px; border: 1px solid #7f8286a8; margin-top: 55px;}
		.item > div {padding: 50px;}
		
	</style>
</head>
<body>
<%@ include file="include/header.jsp" %>
<div class="container">
	<div role="tabpanel">
	
	  <!-- Nav tabs -->
	  <ul class="nav nav-tabs">
	    <li class="active">
	    	<a href="#memberInfo" data-toggle="tab">회원정보 변경</a>
	    </li>
	    <li>
	    	<a href="#memberWithdrawal" data-toggle="tab">회원탈퇴</a>
	    </li>
	  </ul>
	
	  <!-- Tab panes -->
	  <div class="tab-content">
	    <div class="tab-pane active" id="memberInfo">
	    <!-- 계정 이메일 , -->
	    	<form id="form-memberinfo" class="form-horizontal" >
	    	<input type="hidden" name="email" id="email" value="${member.email}">
		    	<div class="table-responsive">
					<table class="table table-hover">
						<tbody>
							<tr>
								<th class="col-xs-3">계정 이메일</th>
								<td class="col-xs-9">${member.email}</td>
							</tr>
							<tr>
								<th class="col-md-3">비밀번호</th>
								<td class="col-xs-9">
									<button class="btn btn-secondary" type="button" data-toggle="collapse" data-target="#changePwForm" aria-expanded="false" aria-controls="changePwForm">비밀번호 변경</button>
									<div id="changePwForm" class="panel-collapse collapse">
										<div class="form-group">
											<label for="currentPassword" class="col-xs-3">현재 비밀번호</label>
											<div class="col-xs-4"><input type="password" class="form-control" name="currentPassword" id="currentPassword" ></div>
											<button type="button" class="btn btn-default" id="currentPasswordChk">확인</button>
											<!-- 비밀번호 확인체크  -->
											<input type="hidden" name="passwordChkok" id="passwordChkok" >
										</div>
										<div class="form-group">
											<label for="changePassword" class="col-xs-3">새로운 비밀번호</label>
											<div class="col-xs-4"><input type="password" class="form-control" name="password" id="password" ></div>
										</div>
										<div class="form-group">
											<label for="changePasswordChk" class="col-xs-3">새로운 비밀번호 확인</label>
											<div class="col-xs-4"><input type="password" class="form-control" name="changePasswordChk" id="changePasswordChk" ></div>
										</div>
						      			<div class="col-xs-offset-3">
       	 									<button type="button" class="btn btn-default" id="changePasswordBtn">비밀번호 변경</button>
      									</div>
									</div>
								</td>
							</tr>
							<tr>
								<th class="col-md-3">이름</th>
								<td class="col-xs-9">
									<label for="changeName"></label>
									<div class="col-xs-4"><input type="text" class="form-control" name="name" id="name" value="${member.name}"></div>
								</td>
							</tr>
							<tr>
								<th class="col-md-3">성별</th>
								<td class="col-xs-9"> 
								    여자  <input type="radio" name="gender" id="f" value="f" ${member.gender == 'f'?'checked':''}>
								    남자  <input type="radio" name="gender" id="m" value="m">
								</td>
							</tr>
							<tr>
								<th class="col-md-3">생년월일</th>
								<td class="col-xs-9">${member.birthdate}</td>
							</tr>
							<tr>
								<th class="col-md-3">한마디</th>
								<td class="col-xs-9">
									<label for="motto">Comment:</label>
  									<textarea class="form-control" rows="5" id="motto" name="motto">${member.motto}</textarea>		
  								</td>
							</tr>
						</tbody>
					</table>	
	    		</div>
	    		<div><button type="button" class="btn btn-success" id="modifyMemberInfo">수정하기</button></div>
	    	</form>
	    </div>
	    <div class="tab-pane" id="memberWithdrawal">
	    <div class="text-center item"> 
			<div class="shadow-box-example z-depth-1-half ">
	    		<div class="heading">
	    			<h1>회원탈퇴</h1>
	    		</div>
	    		<p>	
	    			HWAZZANG 페이지를 다시 사용할 일이 없어 계정을 없애고 싶으시면 계정 삭제를 처리해드리겠습니다.
	    		         삭제된 계정은 다시 복구할 수 없고 계정의 게시물이나 정보는 완전히 삭제된다는 점을 기억해 주세요.
	    		          그래도 계정을 삭제하려면 '계정 삭제'를 클릭하세요. 
	    		</p>
	    		</div>
	    		<form id="form-signout" class="form-horizontal" action="signout" method="post">
	    			<input type="hidden" name="email" id="email" value="${email}">
					<div class="form-group">				
						<label for="password" class="col-xs-3">비밀번호</label>
						<div class="col-xs-4"><input type="text" class="form-control" name="password" id="password" ></div>
					</div>
	    			<button class="btn btn-secondary">계정 삭제</button>
	    			<button class="btn btn-secondary">취소</button>
	    		</form>
	    		
	    	</div>
	    </div>	
	   </div>
	 </div>
</div>

<%@ include file="include/footer.jsp" %>
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/mypage.js"></script>
</body>
</html>





