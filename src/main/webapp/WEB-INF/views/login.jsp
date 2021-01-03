<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Login</title>
	
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/css/common.css" type="text/css">
	<link rel="stylesheet" href="assets/css/login.css" type="text/css">
</head>
<body>
	<!-- join modal -->
	<div class="modal fade" id="modal-join">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
 					<h2 class="modal-title">Welcome To ILHWA's Web.</h2>
 				</div>
				<form id="form-join" class="form-horizontal">
					<div class="modal-body">
						<div class="form-group">
							<!-- e-mail -->
							<label for="email" class="col-xs-3 control-label">e-mail</label>
							<div class="col-xs-6"><input type="email" class="form-control"  id="email"  name="email" placeholder="e-mail"></div>
							<!-- email dupl chk result-->	
							<button type="button" class="btn btn-info" onclick="emailDuplCheck();">Dupl Check</button>
							<!-- emailExistCheck true/false-->	
							<input type="hidden" name="emailDuplChkAction" id="emailDuplChkAction">
						</div>
						<br>
						<div class="form-group">
							<!-- pw -->
							<label for="password" class="col-xs-3 control-label">Password</label>
							<div class="col-xs-6"><input type="password" class="form-control" name="password" id="password" placeholder="password"></div>
						</div>
						<div class="form-group">
							<!-- pw chk -->
							<label for="passwordChk" class="col-xs-3 control-label">Password Check</label>
							<div class="col-xs-6"><input type="password" class="form-control" name="passwordChk" placeholder="Password Check"></div>
						</div>
						<br>
						<div class="form-group">
							<!-- name -->
							<label for="name" class="col-xs-3 control-label">Name</label>
							<div class="col-xs-6"><input type="text" class="form-control" name="name" placeholder="Name"></div>
						</div>
						<div class="form-group">
							<!-- gender -->
							<label for="gender" class="col-xs-3 control-label">Gender</label>	
							<div class="radio">
								<label for="man" class="col-xs-offset-1">
									<input type="radio" name="gender" value="m" checked>Man
						  		</label>
								<label for="woman" class="col-xs-offset-1">
									<input type="radio" name="gender" value="w">Woman
						  		</label>
							</div>
						</div>
						<div class="form-group">
							<!-- birthdate -->
							<label for="birthdate" class="col-xs-3 control-label">Birthdate</label>
							<div class="col-xs-6"><input type="date" class="form-control" name="birthdate" placeholder="Birthdate"></div>
						</div>
						<div class="form-group">
							<!-- motto -->
							<label for="motto" class="col-xs-3 control-label">Motto</label>
							<div class="col-xs-6"><input type="text" class="form-control" name="motto" placeholder="Motto"></div>
						</div>
						<div class="form-group">
							<div class="checkbox">
								<span class="col-xs-offset-7">I agree to the terms.</span>
								<input type="checkbox" class="col-xs-2" value="y">
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="submit" class="btn btn-default" data-dismiss="modal">close</button>
						<button type="button" class="btn btn-success" onclick="join();">회원가입</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- finding email modal -->
	<div class="modal fade" id="modal-find-email">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
 					<h2 class="modal-title">Finding email</h2>
 				</div>
 				<div id="modal-content-container">
					<form id="form-find-email" class="form-horizontal">
						<div class="modal-body">
							<div class="form-group">
								<!-- name -->
								<label for="name" class="col-xs-3 control-label">Name</label>
								<div class="col-xs-6"><input type="text" class="form-control" name="name" placeholder="Name"></div>
							</div>
							<div class="form-group">
								<!-- birthdate -->
								<label for="birthdate" class="col-xs-3 control-label">Birthdate</label>
								<div class="col-xs-6"><input type="date" class="form-control" name="birthdate" placeholder="Birthdate"></div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
							<button type="button" class="btn btn-success" onclick="findEmail();">submit</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- modal-find-result-email -->
	<div class="modal fade" id="modal-find-result-email">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
 					<h2 class="modal-title">Finding email</h2>
 				</div>
 				<div id="modal-content-container">
					<table id='findEmailList'>
						<tr>
							<td></td>
						</tr>
					</table>
					<button class='btn btn-primary'>로그인 하기</button>
					<button type='button' id='closebtn'>닫기</button>	
				</div>
			</div>
		</div>
	</div>
	
	<!-- finding password modal -->
	<div class="modal fade" id="modal-find-pw">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
 					<h2 class="modal-title">Finding Password</h2>
 				</div>
				<form id="form-find-pw" class="form-horizontal" method="POST">
					<div class="modal-body">
						<div class="form-group">
							<!-- e-mail -->
							<label for="email" class="col-xs-3 control-label">e-mail</label>
							<!-- id와   name이 다른 이유 : 서버로 보내는 데이터를 구분 하기 위해 -> name: email로 보내면 null값으로 된다.-->
							<div class="col-xs-6"><input type="email" class="form-control" name="email"  placeholder="ID(E-mail형식)"></div>
							<!-- emailExistCheck true/false : front 에서 check-->	
						</div>
						<div class="form-group">
							<label for="name" class="col-xs-3 control-label">name</label>
							<div class="col-xs-6"><input type="text" class="form-control" name="name" placeholder="name"></div>
						</div>
						<div class="form-group">
							<label for="recipient" class="col-xs-3 control-label">recipient</label>
							<div class="col-xs-6"><input type="email" class="form-control" name="recipient" placeholder="recipient"></div>
							<button type="button" class="btn btn-success" onclick="sendVerificationCode()">전송</button>
						</div>
						<div class="form-group">
							<!-- verification code -->
							<label for="recipient" class="col-xs-3 control-label">Verification Code</label>
							<div class="col-xs-6"><input type="text" class="form-control" placeholder="verificationCode" name="verificationCode"></div>
							<!--receiveVerificationCode  true/false : 서버에서 받은 값으로 front 에서 check  ${receiveVerificationCode}-->
							<input type="hidden" name="receiveVerificationCode" id="receiveVerificationCode">
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
						<button type="button" class="btn btn-info" onclick="finalValueCheckForPwReset()">확인</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- reset password modal -->
	<div class="modal fade"  id="modal-reset-password">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
 					<h2 class="modal-title">Reset password</h2>
 				</div>
 				<div id="modal-content-container">
					<form id="form-reset-password" class="form-horizontal">
						<div class="modal-body">
							<div class="form-group">
								<!-- NewPassword -->
								<label for="newPassword" class="col-xs-4 control-label">NewPassword</label>
								<div class="col-xs-6"><input type="password" class="form-control" name="password" placeholder="password" id="newPassword"></div>
							</div>
							<div class="form-group">
								<!-- PasswordConfirm -->
								<label for="confirmPassword" class="col-xs-4 control-label">confirm</label>
								<div class="col-xs-6"><input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="ConfirmPassword"></div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-success" onclick="resetPassword()">submit</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- login form -->
	<div class="container">
		<form id="form-login" action="signIn" method="post">
			<div class="IH-title">ILHWA's Portfolio</div>
   			<div class="form-group">
   				<!-- e-mail -->
   				<label for="email" class="sr-only">e-mail</label> 
     			<input type="email" class="form-control" name="email" placeholder="e-mail" value="${userEmailInCookie}">
   			</div>	
   			<div class="form-group">
   				<!-- password -->
   				<label for="email" class="sr-only">Password</label> 
     				<input type="password" class="form-control" name="password" placeholder="Password">
			</div>
   			<div class="checkbox">
				<input type="checkbox" class="col-xs-2" name="emailRememberChecked">
				<span class="col-xs-offset-1">Remember me.</span>
			</div>
   			<div class="IH-btn">
   				<button type="submit" class="btn btn-primary">Sign In</button>
   				<button type="button" class="btn btn-success" data-toggle="modal" data-target="#modal-join">Join Us</button>
   			</div>
		</form>
		<div class="IH-link">
   				I forget my <strong><a data-toggle="modal" data-target="#modal-find-email">e-mail</a></strong> / <strong><a data-toggle="modal" data-target="#modal-find-pw">password</a></strong>.
   		</div>
	</div>

	<%@ include file="include/footer.jsp" %>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="assets/js/login.js"></script>
</body>
</html>