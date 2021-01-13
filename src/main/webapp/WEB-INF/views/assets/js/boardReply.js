
	// 회원 댓글 수정 
	$(document).on('click', '.memberReplyUpdate', function() {
		replyUpdate($(this));
	});   
	
	// 댓글 수정 취소
	$(document).on('click', '.updateReplyCanclebtn', function() {
		updateCancle();
	});
	
	// 대댓글 입력창
	$(document).on('click', '.replyReplyBtn', function() {
		var replynum = $(this).parent().data('idx');
		$('input[name="parent"]').val(replynum);
		$(this).parents('li').append($('.reply-reply-box').html());
	});
	
	// 댓글 목록 함수 
	function getReplies(repliesUri){
		//$.getJSON(url,callback) :  ajax()의 축약 형태 -> json데이터를 받아옴
		var url = repliesUri+'?boardnum='+boardnum;
		$.getJSON(url, function (data) {
				printReplies(data);
				}); 
	}

	// 댓글 목록 출력 함수 
	function printReplies(replyArr) {
		if(replyArr.replyList.length != 0){
			// 핸들바 사용순서 : 핸들바 템플릿을 가져온다. -> 핸들바 템플릿을 컴파일한다.
			var replyTemplate = Handlebars.compile($('#reply-Template').html());
			var result = replyTemplate(replyArr.replyList);
			$('#replyprint').children('li').remove();
			$('#replyprint').append(result);
		}
	}
	
	// 댓글 수정
	function replyUpdate(target){
		var key =  $(target).data('key'); 
		var replynum = $(target).data('idx');
		
		$('.updateReplynum').val(replynum);
		$('.updateReply').text($(".reply"+key).text());
		$(target).closest('li').children('div:not(:first-child)').hide();  
		$(target).closest('li').append($('#reply-update-box').html());
	}
	
	// 댓글 수정 취소
	function updateCancle(){
		if($('#reply-update-form').is(':visible')){ 
			$('.reply').children(':hidden').show(); 
			$('#reply-update-form').remove();
		}
	}
	
	// 댓글 삭제 결과
	function replyDelete(thisEle){
		var target = $(thisEle).closest('li');
		if(target.next().data('depth') != 0){
			target.html( '<div>삭제된 댓글 입니다.</div>' ); // 부모 댓글 삭제
		}else{
			target.remove(); // 자식 댓글 삭제
		}
	}
	
	// 비회원 댓글 (수정/삭제) 비밀번호 팝업
	$(document).ready(function(){
		
		$(document).on('click', '.passwordChkBtn', function(e) {
			updateCancle();          // 열려있는 수정 form 닫기
			$('.passwordChkBtn').not($(this)).popover('hide');  // 비밀번호 확인 popover 닫기
			$(this).popover({
				placement : 'Right',
				html : true,
				sanitize : false,
				content :function(){
               			var content = '';
               			content = $('#password-Chk-form').html();
               			return content;
            			}
			}).popover('show');
				
		 	$(document).on('shown.bs.popover', function(event){	
				$('.replyPasswordChkBtn').addClass(event.target.getAttribute('value'));       // update or delete 
				$('.replyPasswordChkBtn').data('idx', event.target.getAttribute('data-idx')); // 댓글 pk 
				$('.replyPasswordChkBtn').data('key', event.target.getAttribute('data-key')); // 댓글 인덱스
			});
		});
		
		$(document).on('click', '.replyPasswordChkCloseBtn', function(e) {
			$(this).parents('.popover').popover('hide');
		});
		
	});		
	
	// 비회원 댓글 비밀번호 확인
	$(document).on('click', '.replyPasswordChkBtn', function() {
		$(this).parents('.popover').popover('hide');
		
		var value = $(this).hasClass('update') ? 'update' : 'delete';
		
		var data  = { replynum :      $(this).data('idx'),            
			  	      reply_password :$('#reply_password-chk').val(),
	       	 	      value :         value
					}
		$.ajax({
		    url:'replyPwCheck', 
		    type:'POST',
			//contentType : 서버에 데이터를 보낼 때 사용  -> contentType을 json으로 보낼시  JSON.stringify()로 감싸 주어야지 "key":"value"로 인식 
		    contentType: 'application/json;charset=UTF-8',
		    data: JSON.stringify(data),
			context: this,              // ajax에서 this는 response이기 떄문에 이 설정을 해줘야지 $(this)사용할수 있따.
		    dataType:'text',        	// 서버에서 반환되는 데이터 형식 xml, json, script, html
		    success:function(data) {
				if(data != '0'){
					if(value == 'update'){
						replyUpdate($(this)); // 댓글 수정
					}else{
						replyDelete($(this));    // 댓글 삭제
					}
		    	}else{
					alert('확인번호를 다시 입력해주세요.');
					}	
		    },
		    error:function(jqXHR) {}
		});
	})


	// 회원 댓글 삭제
	$(document).on('click', '.memberReplyDelete', function() {   
		$.ajax({
			type : 'POST', 
			url : 'memberReplyDelete', 
			data: {replynum : $(this).data('idx')},
			context: this,
			success : function (result) {
				replyDelete($(this).closest('li'));
				}
			});
		});
 

	// 댓글 수정 do
	$(document).on('click', '.updateReplybtn', function() {
		$.ajax({ 
			type : 'POST', 
			url : 'replyUpdate', 
			data :$('#reply-update-form').serialize(),
			dataType : "text", 
			success : function (result) { 
				if (result != "0") {
					getReplies("replyList");
				}
			} 
		}); 
	});
	
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
				getReplies("replyList"); // 댓글 목록 함수 호출
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
				// 댓글 목록 함수 호출 
				getReplies("replyList");
			}
		});
  	});
	


