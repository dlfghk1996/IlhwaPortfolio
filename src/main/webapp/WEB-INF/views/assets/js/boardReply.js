	// 댓글 (수정/삭제) 비밀번호 팝업
	$(document).ready(function(){
		$(document).on('click', '.passwordChkBtn', function(e) {
			if($(this).data('toggle') == ''){
				//회원이 본인 댓글 삭제
				memberReplyDelete($(this).data('idx'));
			}
			
		 	$('.passwordChkBtn').not($(this)).popover('hide');
			$(this).popover({
				placement : 'Right',
				//trigger: 'toggle', //click
				html : true,
				sanitize : false,
				content :function(){
               			var content = '';
               			content = $('#password-Chk-form').html();
               			return content;
            			}
			}).popover('show');
				
		 	$(document).on('shown.bs.popover', function(event){	
				var replynum = event.target.getAttribute('data-idx');
				var replykey = event.target.getAttribute('data-key');
				var buttonValue = event.target.getAttribute('value');
				$('.replyPasswordChkBtn').addClass(buttonValue);
				$("input[name='replynum']").val(replynum);
				$(".replyPasswordChkBtn").data('key', replykey);
			});
		});
		$(document).on('click', '.replyPasswordChkCloseBtn', function(e) {
			alert('닫기버튼뉴룸');
			$(this).parents('.popover').popover('hide');
		});
	});			

	


	
	// 댓글 비밀번호 확인
	$(document).on('click', '.replyPasswordChkBtn', function(e) {
		$(this).parents('.popover').popover('hide');
		var thisEle = $(this).closest('li');
		var key = $(e.target).data('key');
		var replynum = $('input[name=replynum]').val(); //댓글 pk
		var reply_password = $('#reply_password-chk').val(); //댓글 비밀번호
		var value = $(this).hasClass("update") ? "update" : "delete";
	
		var data = {replynum : replynum,
			  	reply_password :reply_password,
	       	 	value : value
	          }
	$.ajax({
	    url:'replyPwCheck', 
	    type:'POST', 
	    contentType: 'application/json;charset=UTF-8',//contentType : 서버에 데이터를 보낼 때 사용  // contentType을 json으로 보낼시  JSON.stringify()로 감싸 주어야지 "key":"value"로 인식
	    data: JSON.stringify(data),
		context: this,  // ajax에서 this는 response이기 떄문에 이 설정을 해줘야지 $(this)사용할수 있따.
	    dataType:'text',// 서버에서 반환되는 데이터 형식 xml, json, script, html
	    success:function(data) {
		alert(data);
	    	if(data != '0'){
	    		if(data == '2'){
					// 댓글 삭제
	    			thisEle.remove();
	    			alert('댓글이 삭제 되었씁니다.'); 
	    		}else{
					// 수정 기능
	    			var reply = $(".reply"+key).text();
			    	// 댓글 내용
			    	$(".reply"+key).html("<textarea name='reply' id='editContent' class='form-control' rows='3'>"+ reply +"</textarea>");
			    	// 댓글  (수정/삭제)  버튼
			    	$(".replyoOptionBox"+key).html("<a href='#' class='updateReplybtn' data-idx='"+replynum+"'>완료</a> "+"<a href='#'>취소</a>");
	    		}
			}else{
				if(thisEle.next().data('depth') > thisEle.data('depth') && value == "delete"){
				alert('삭제 할 수 없는 댓글 입니다.');
				return;
				}
	    		alert('확인번호를 다시 입력해주세요.');
	    	}
	    },
	    error:function(jqXHR) {
	    	
	    }
	});
})
	// 회원 댓글 삭제
	function memberReplyDelete(replynum){
		$.ajax({ 
			type : 'POST', 
			url : 'replyDelete', 
			contentType: 'application/json',
			data : JSON.stringify({replynum:replynum}), 
			dataType : "text", 
			success : function (result) { 
				if (result != "0") {
					alert("댓글을 삭제할 수 업습니다.!"); 
					} 
				} 
		}); 
	}
	
	// 부모댓글에 속해있는 대댓글 삭제
	//function replydelte(thisele){
		//var result = thisele.next();
		//$(thisele).nextUntil(result.data("depth")==0).remove();
	//}


	// 댓글 수정
	$(document).on('click', '.updateReplybtn', function() {
	var reply = $(this).closest("li").find('textarea[name=reply]:visible').val();
	var replynum = $(this).data('idx');
	$.ajax({ 
		type : 'POST', 
		url : 'replyUpdate', 
		contentType: 'application/json',
		data : JSON.stringify({reply : reply, replynum:replynum } ), 
		dataType : "text", 
		success : function (result) { 
			if (result != "0") {
				alert("댓글 수정 완료!"); 
				getReplies("replyList");
				} 
			} 
		}); 
	});

    // 댓글 수정 취소

	// 댓글등록
	$('#replyBtn').click(function(){
		
		$.ajax({
		data : $('#reply-form').serialize(),
			type : 'POST',
			url : 'replyWrite',
			success : function(data) {
				alert('댓글이 등록 되었습니다.');
				$('#reply-form')[0].reset();
				// 댓글 목록 함수 호출 
				getReplies("replyList");
			}
		});
  	});
	
	// 대댓글 등록
	$(document).on('click','#reply-reply-btn', function() {
		$.ajax({
			type : 'POST',
			data : $('#reply-reply-form').serialize(),
			url : 'replyWrite',
			success : function(data) {
				alert('댓글이 등록 되었습니다.');
				// 댓글 목록 함수 호출 
				getReplies("replyList");
			}
		});
  	});
	
	// 대댓글 입력창
	$(document).on('click', '.replyReplyBtn', function() {
		var replynum = $(this).parent().data('idx');
		$("input[name='parent']").val(replynum);
		$(this).parents('li').append($('.reply-reply-box').html());
	});


	// 댓글 목록 함수 
	function getReplies(repliesUri){
		//$.getJSON(url,callback) :  ajax()의 축약 형태 -> json데이터를 받아옴
		var url = repliesUri+"?boardnum="+boardnum;
		$.getJSON(url, function (data) {
			printReplies(data);  
		 	}); 
		}

	// 댓글 목록 출력 함수 
	function printReplies(replyArr) {
		if(replyArr.replyList.length == 0){
			console.log("조회 댓글 없음")
		} else {
		// 핸들바 사용순서 : 핸들바 템플릿을 가져온다. -> 핸들바 템플릿을 컴파일한다.
		var replyTemplate = Handlebars.compile($("#reply-Template").html());
		var result = replyTemplate(replyArr.replyList);
		$("#replyprint li:last-child").remove();
		$("#replyprint").append(result);
		}
	}
