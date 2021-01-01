<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link  rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
<link rel="stylesheet" href="assets/css/common.css" type="text/css">
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/board.js"></script>
<script src="assets/js/jquery.MultiFile.min.js"></script>
<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<style type="text/css">
	#uploadList{
		background-color: #cccccc0d;
    	border: 1px solid #b5b1b159;
    	height: 100px;
    	padding: 10px 35px;
    	list-style: decimal;
	}
</style>
</head>
<body>
<%@ include file="../include/header.jsp" %>
		<!-- 새 글 작성 -->
<div class="container">
	 <div class="row">
	 <form id="boardForm" method="POST" action="postContent" enctype="multipart/form-data">
	 	<c:set var ="memberDTO" value="${members}" />
			<c:if test="${memberDTO == null}">
				 <div class="form-group">
					<!-- 회원이 아닌경우 -->
	    			<label for="writerid">이름</label>
	    			<input type="text" class="form-control" id="writerid" name="writerid" maxlength="32">
	    			<label for="pwd">비밀번호</label>
	    			<input type="password" class="form-control" id="pwd" name="pwd" maxlength="32">
				 </div>
			</c:if>
			<div class="form-group">
    			<label for="subject">제목</label>
    			<input type="text" class="form-control" id="subject" name="subject" maxlength="32">
  			</div>
  			<div class="form-group">
  			    <!-- 첨부 파일 -->
    			<label for="subject">첨부파일</label>
    			<input type="file" class="form-control multi" id="file" name="file">
    			<!-- 첨부파일 리스트 -->
				<ul id="uploadList">
				</ul>
  			</div>
			<textarea name="content" id="summernote" class="content"></textarea>
			<div class="row">
				<div class="col-md-offset-10 col-md-1"><button type="button" class="btn btn-primary" >목록</button></div>
				<div class="col-md-1"><button type="submit" class="btn btn-success">등록</button></div>
			</div>
		</form>
	</div>
</div>

<%@ include file="../include/footer.jsp" %>

</body>
</html>