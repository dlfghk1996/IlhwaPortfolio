<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link  rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" type="text/css">
<style type="css/text">
	body{height: 100%;}
</style>

</head>
<body>
<%@ include file="../include/header.jsp" %>
<div class="container">
	<form method="get" action="board">
		<select name="keyword">
			<option value="all" <c:out value="${board.keyword =='all'?'selected':''}"/>>----</option>
			<option value="subject" <c:out value="${board.keyword =='subject'?'selected':''}"/>>제목</option>
			<option value="writerid" <c:out value="${board.keyword =='writerid'?'selected':''}"/>>작성자</option>
		</select>
		<input type="text" name="search" placeholder="검색" value="${board.search}">
		<button type="submit" id="borderSearchBtn">검색</button>
	</form>
	 <div class="row">
		<table class="table table-hover">
		 	<thead>
		 		<tr>
		 			<th>번호</th>
		 			<th>제목</th>
		 			<th>작성자</th>
		 			<th>작성일자</th>
		 			<th>조회수</th>
				</tr>
		 	</thead>
		 	<tbody>
		 		<c:choose>
		 			<c:when test="${empty contentList}">
		 				<tr>
		 					<td>게시판이 비어있습니다.</td>
		 				</tr>
		 			</c:when>
		 			<c:otherwise>
			 			 <c:forEach var="boardDTO" items="${contentList}">
			 				<tr>
			 					<td>${boardDTO.boardnum}</td>
			 					<td>
			 						<a href="boardView?boardnum=${boardDTO.boardnum}">
			 							${boardDTO.subject} 
			 							<c:if test = "${boardDTO.recount>0}">
			 								[${boardDTO.recount}]
			 							</c:if>
			 						</a>
			 					</td>
			 					<td>${boardDTO.writerid}</td>
			 					<td>${boardDTO.regdate}</td>
			 					<td>${boardDTO.readnum}</td>
			 				</tr>
			 			</c:forEach>
		 			</c:otherwise>
		 		</c:choose>
			</tbody>
		</table>
		<button type="button" class="btn btn-light"  onclick="location.href='boardwrite'">글쓰기</button>
		<div>${pagenation}</div>
	</div>
</div>


<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
<%@ include file="../include/footer.jsp" %>
</body>
</html>