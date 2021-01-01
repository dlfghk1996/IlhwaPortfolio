<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link  rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<link rel="stylesheet" href="assets/css/common.css" type="text/css">
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
<div class="container-fluid text-center">
	<div class="row">
		<h1>자유게시판</h1>
	   <form id="boardForm" method="POST" action="updateContent" enctype="multipart/form-data">
	   	<input type="hidden" name="boardnum" value="${board.boardnum}">
			<div class="form-group">
    			<label for="subject">제목</label>
    			<input type="text" class="form-control" id="subject" name="subject" maxlength="32" value="${board.subject}">
  			</div>
  			<div class="form-group">
  			    <!-- 첨부 파일 -->
    			<label for="file">첨부파일</label>
    			<input type="file" class="form-control multi" id="file" name="file">
    			<!-- 첨부파일 리스트 -->
				<ul id="uploadList">
  					<c:if test="${not empty boardfile }">
  					<c:forEach items="${boardfile}" var="b">
  						<li>
  							<!-- 기존파일 input:hidden -->
  							<input type="hidden" name="files" value="${b.filenum}"> 
  							<a href="#" class="fileDeleteBtn" data-value=" ${b.filenum}" data-value2="${b.filepath}">
  							    [삭제]&nbsp;&nbsp; ${b.originalfilename}
  							</a>
  						</li>
  					</c:forEach>
  				    </c:if>
    			</ul>
				</div>
  				<textarea name="content" id="summernote" class="content">${board.content}</textarea>
			<div class="row">
				<div class="col-md-1"><button type="submit" class="btn btn-success">등록</button></div>
			</div>
	   </form>
	</div>
</div>
<%@ include file="../include/footer.jsp" %>


</body>
</html>