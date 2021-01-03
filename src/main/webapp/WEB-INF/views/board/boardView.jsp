<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link  rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Atma:wght@500&display=swap">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="assets/css/common.css" type="text/css">
<title>boardView</title>

<style type="text/css">
	.container-fluid {width : 1200px;}
	.contentInfo2 li {display : inline-block;}
	.contentInfo1 {float : left; width : 80%; text-align : left;}
	h1, h3 {text-align : left;}
	input {width : 100%;}
	.comment {margin-top : 30px;;}
	#reply {border-bottom : 1px solid lightgrey; padding : 15px 0;}
   .reply-reply-box {background-color : #d3d3d330; float : left; width : 100%; padding : 0 50px 0 100px;}
</style>

</head>

<body>
<%@ include file="../include/header.jsp" %>
<div class="container-fluid text-center">
	<div class="row">
		<h1>자유게시판</h1>
		<div class="panel-group">
			<div class="panel panel-default">
				 <!-- 조회수, 댓글 수, 글제목, 작성자  -->
				 <div class="panel-heading">
				 	<h3>${board.subject}</h3>
				 	<div class="contentInfo1">
				 		<span>작성자이름   &nbsp; | ${board.regdate}</span>
				 	</div>
				 	<div class="contentInfo2">
				 		<ul>
				 			<li><span>조회  &nbsp;&nbsp;</span>${board.readnum}</li>
				 			<li><span>추천 수  &nbsp;&nbsp;</span>${board.hit}</li>
				 			<li>댓글 수 </li>
				 		</ul>
				 	</div>
				 </div>
				 <div>
				 	<ul>	
				 	<c:if test="${not empty attachFileList}">
				 	 	<c:forEach items="${attachFileList}" var="filelist">
							<li><a href="attachFileDownload?filepath=${filelist.filepath}">${filelist.originalfilename}</a></li>
						</c:forEach>
				 	 </c:if> 
				 	 </ul>
				 </div>
				 <!-- 글 내용 -->
      			 <div class="panel-body">
      			 	<p>${board.content}</p>	
      			 </div>
      			
				 <c:if test="${members.membernum eq board.writer && not empty members.membernum}">
				 	<button type="button" class="btn btn-success pull-righ t" onclick="location.href='boardUpdate?boardnum=${board.boardnum}'">글 수정</button>
				 	<button>글 삭제</button>
				 </c:if>
				  <!-- 비회원 일경우  비밀번호 입력-->
      			 <c:if test="${not empty board.pwd}">
					<button type="button" class="btn btn-success pull-right" data-toggle="modal" data-target="#modal-nonMember-PwChk">글 수정</button>
      			 	<button type="button" class="btn btn-success pull-right" data-toggle="modal" data-target="#modal-nonMember-PwChk">글 삭제</button>
      			 </c:if>
			</div>						
		</div>
	</div>
	<!-- Modal -->
	<div class="modal fade" id="modal-nonMember-PwChk" role="dialog">
    	<div class="modal-dialog">
		<!-- Modal content-->
	      	<div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal">&times;</button>
		          <h4>비밀번호 확인</h4>
		        </div>
		        <div class="modal-body" style="padding:40px 50px;">
		          <form id="form-nonMember-PwChk"role="form" action="nonMemberPwChk" method="POST">
		          	<div class="form-group">
		              <input type="hidden" name="boardnum" value="${board.boardnum}">
		              <label for="pwd"><span class="glyphicon glyphicon-eye-open"></span> Password </label>
		              <input type="text" class="form-control" id="pwd" name ="pwd" placeholder="password">
		            </div>
					<button type="submit" class="btn btn-success btn-block button-nonMember-PwChk" >확인</button>
		          </form>
		        </div>
		        <div class="modal-footer">
		         	<button type="button" class="btn btn-default" data-dismiss="modal">close</button>
		        </div>
			</div>
  		</div> 
	</div>
	
	<!-- 댓글 -->
	<form id="reply-form">
	<c:if test="${members != null}">
		<input type="hidden" value="${members.membernum}" name="reply_writer">
		<input type="hidden" value="${members.name}" name="reply_writer_nickname">
	</c:if>
	<input type="hidden" value="${board.boardnum}" name="reply_boardnum">
	<c:if test="${members == null}">
		<div class="form-group">
			<div class="form-inline col-xs-6">
				<label for="reply_writer_nickname" class="col-xs-2">닉네임 :</label>
      			<div class="col-xs-9"><input type="text" class="form-control " id="reply_writer_nickname" placeholder="닉네임" name="reply_writer_nickname" width="100%" ></div>
			</div>
			<div class="form-inline col-xs-6">
				<label for="reply_password"  class="col-xs-2">Password:</label>
      			<div class="col-xs-9"><input type="password" class="form-control" id="reply_password" placeholder="password" name="reply_password"></div>
	   		</div>
	   	</div>
	</c:if>
		<div class="form-group col-xs-12 comment">
  			<label for="reply" class="col-xs-2">Comment:</label>
  			<div class="col-xs-8"><textarea class="form-control" rows="3" cols="30" id="reply" name="reply" placeholder="댓글을 입력하세요"></textarea></div>
  			<button type="button" class="btn btn-default" id="replyBtn">댓글 등록</button>
		</div>
	</form>
	<div class="clear"></div>
	
	<ul id="replyprint">
	<script id="reply-Template" type="text/x-handlebars-template">
	{{#each.}}
		{{#if del}}
			<div>삭제된 댓글 입니다.</div>
		{{else}}
			<li class="reply row" data-depth ="{{depth}}" style="margin-left: calc(20px*{{depth}});">
					<div class="writerbox col-xs-3"><span>{{reply_writer_nickname}}</span></div>
					<div class="col-xs-7 reply{{@key}} replyText">{{reply}}</div>
					<div class="col-xs-2">
						<div>{{regdate}}</div>
						<div id="test" class="replyoOptionBox{{@key}}" data-idx="{{replynum}}">
							{{{buttonFunction reply_writer @key replynum depth}}}
					</div>
				</div>
			</li>	
		{{/if}}
	{{/each}}
	</script>
	</ul>
	
	<!-- 비밀번호 확인 -->
	<div id="password-Chk-form" style="display:none;">
	    <input type="hidden" name="replynum">
		<input type="text" id="reply_password-chk" name="reply_password" style="width:200px;">
    	<button type="button" class="btn btn-default replyPasswordChkBtn">확인</button>                            
    	<button type="button" class="replyPasswordChkCloseBtn" ><i class="fas fa-window-close"></i></button>
	</div>
	
	<!-- 대댓글 -->
	<div class="reply-reply-box" style="display:none;"> 
	 	<div>
			<form id="reply-reply-form">
			<input type="hidden" name="parent">
			<input type="hidden" name="reply_boardnum" value="${board.boardnum}">
			<c:if test="${members == null}">
				<div class="form-group">
					<div class="form-inline col-xs-6">
						<label for="reply_writer_nickname"></label>
		      			<input type="text" class="form-control " placeholder="닉네임" name="reply_writer_nickname">
		      			<label for="reply_password"></label>
		      			<input type="password" class="form-control" placeholder="password" name="reply_password">
					</div>
					<div><button type="button" class="btn btn-default" id="reply-reply-btn">댓글 등록</button></div>
				</div>
			</c:if>
				<div class="form-group comment">
		  			<label for="reply"></label>
		  			<textarea class="form-control" rows="3" cols="30" name="reply" placeholder="댓글을 입력하세요"></textarea>
				</div>
			</form>
		</div>
	</div>
	<c:choose>
		<c:when test="${!empty prevNextMap.PREV}">
			<div>
			         이전글
				<a href="boardView?boardnum=${prevNextMap.PREV}">
					<span>${prevNextMap.PREV}</span>
					<span>${prevNextMap.PREVSUBJECT}</span>
				</a>
			</div>
		</c:when>
		<c:otherwise>
			이전 게시글이 없습니다.
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty prevNextMap.NEXT}">
			<div>
				다음글
				<a href="boardView?boardnum=${prevNextMap.NEXT}">
					<span>${prevNextMap.NEXT}</span>
					<span>${prevNextMap.NEXTSUBJECT}</span>
				</a>
			</div>
		</c:when>
		<c:otherwise>
			다음 게시글이 없습니다.
		</c:otherwise>
	</c:choose>
</div>	
<%@ include file="../include/footer.jsp" %>
</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.7.6/handlebars.min.js"></script>
<script src="assets/js/boardReply.js"></script>
<script type="text/javascript">
	
 	var boardnum = "<c:out value='${board.boardnum}'/>";
	getReplies("replyList"); // 댓글 목록 함수 호출 
	
	
	
	Handlebars.registerHelper('buttonFunction', function(reply_writer,index,replynum,depth){
		var membernum ="<c:out value='${empty members.membernum?0:members.membernum}'/>";
		var result;

		var update = '<button type="button" value="update" class="btn btn-primary passwordChkBtn update" data-key="'+ index +'" data-idx="'+ replynum +'" data-toggle="popover">수정</button>';
		var del = '<button type="button" value="delete" class="passwordChkBtn delete" data-key="'+ index +'" data-idx="'+ replynum +'" data-toggle="popover">삭제</button>';
		var replyAdd ='';
		// 현재 이용자가 비회원이면서 댓글작성자 비회원
		if(depth != 1){
			replyAdd = membernum +','+reply_writer+ '<button type="button" class="replyReplyBtn" data-key="'+ index +'"  data-idx="'+ replynum +'"><i class="fas fa-plus-circle"></i></button>';
		}
		// 현재 이용자가 비회원이면서 댓글작성자 비회원	
		if(membernum == 0 && reply_writer == 0){
			result = update+del+replyAdd;	
		// 현재 이용자가 비회원이면서 댓글작성자 회원	
		}else if(membernum == 0 && reply_writer > 0){
			return;
		// 현재 이용자가 회원이면서 댓글작성자와 현재이용자가 같음
		}else if(membernum > 0 && reply_writer == membernum){
			var mupdate = '<button type="button" value="update" class="memberReplyDelete btn btn-primary update" data-key="'+index+'" data-idx="'+replynum+'">수정</button>';
			var mdel = '<button type="button" value="delete" class="memberReplyDelete delete" data-key="'+index+'"data-idx="'+replynum+'">삭제</button>';
			result = mupdate+mdel+replyAdd;
		}else{
			return;
		}
		return result;
	})
</script>
</html>