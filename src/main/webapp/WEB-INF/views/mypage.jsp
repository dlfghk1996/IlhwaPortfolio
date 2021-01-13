<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>MyPage</title>

	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/css/common.css" type="text/css">
	<link rel="stylesheet" href="assets/css/mypage.css" type="text/css">
</head>
<body>
	<%@ include file="include/header.jsp"%>
	<div class="container">
		<h1 class="page-header">회원정보 수정</h1>

		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingOne">
					<h4 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">개인정보 수정</a></h4>
				</div>
				<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
					<div class="panel-body">
						<form id="form-join" class="form-horizontal">
							<div class="form-group">
								<!-- e-mail -->
								<label for="email" class="col-xs-3 control-label">e-mail</label>
								<div class="IH-readonly-text col-xs-6">${member.email}</div>
							</div>
							<br>
							<div class="form-group">
								<!-- name -->
								<label for="name" class="col-xs-3 control-label">Name</label>
								<div class="col-xs-6">
									<input type="text" class="form-control" name="name" placeholder="Name" value="${member.name}">
								</div>
							</div>
							<div class="form-group">
								<!-- gender -->
								<label for="gender" class="col-xs-3 control-label">Gender</label>
								<div class="radio">
									<label for="man" class="col-xs-offset-1">
										<input type="radio" name="gender" value="m" <c:if test="${member.gender == 'm'}">checked</c:if>>Man
									</label> 
									<label for="woman" class="col-xs-offset-1">
										<input type="radio" name="gender" value="f" <c:if test="${member.gender == 'f'}">checked</c:if>>Woman
									</label>
								</div>
							</div>
							<div class="form-group">
								<!-- birth date -->
								<label for="birthdate" class="col-xs-3 control-label">BirthDate</label>
								<div class="col-xs-6">
									<fmt:parseDate value="${member.birthdate}" var="birthdate" pattern="yyyy-MM-dd HH:mm:ss.S"/>
									<input type="date" class="form-control" name="birthdate" value="<fmt:formatDate value='${birthdate}' pattern='yyyy-MM-dd'/>">
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-success" onclick="join();">저장</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingTwo">
					<h4 class="panel-title"><a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">비밀번호 수정</a></h4>
				</div>
				<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
					<div class="panel-body">
						<form id="form-join" class="form-horizontal">
							<div class="form-group">
								<!-- old pw -->
								<label for="currentPassword" class="col-xs-3 control-label">기존 비밀번호</label>
								<div class="col-xs-6">
									<input type="password" class="form-control" name="currentPassword" placeholder="Old Password">
								</div>
							</div>
							<div class="form-group">
								<!-- new pw -->
								<label for="changePassword" class="col-xs-3 control-label">새로운 비밀번호</label>
								<div class="col-xs-6">
									<input type="password" class="form-control" name="changePassword" placeholder="New Password">
								</div>
							</div>
							<div class="form-group">
								<!-- new pw chk -->
								<label for="changePasswordChk" class="col-xs-3 control-label">새로운 비밀번호 확인</label>
								<div class="col-xs-6">
									<input type="password" class="form-control" name="changePasswordChk" placeholder="New Password Check">
								</div>
								</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-success" onclick="join();">저장</button>
							</div>
							
						</form>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingThree">
					<h4 class="panel-title"><a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">회원 탈퇴</a></h4>
				</div>
				<div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
					<div class="panel-body">
						
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="include/footer.jsp"%>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="assets/js/common.js"></script>
	<script src="assets/js/mypage.js"></script>
</body>
</html>