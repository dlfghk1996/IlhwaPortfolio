    // 회원 게시글 삭제 및 수정
	$('.button-contentOption').click(function(){
		updateAndDelete($(this),'#content-form');
	 });	

	// 비회원 게시글 삭제  및 수정 비밀번호 확인
	$('.button-nonMember-PwChk').click(function(){
		updateAndDelete($(this),'#form-nonMember-PwChk');
	 });
	
	// 비회원 비밀번호 확인 modal창에  클릭이벤트 data(delete/update) 넘기기
	  $('#modal-nonMember-PwChk').on('show.bs.modal', function(event) {          
          var btnValue = $(event.relatedTarget).data('value');
          $('.button-nonMember-PwChk').attr('value',btnValue);
	})
	
	// 게시글 삭제  및 수정 모듈
	function updateAndDelete(target,form){
		if($(target).val() == "delete"){
			url = 'deleteContent';
			if(confirm("게시물을 삭제 하시겠습니까? 게시물을 삭제하시면, 해당 게시물의 댓글도 삭제됩니다.")){
				url = 'deleteContent';		
			}	
		}else{
			url = 'boardUpdate';
		}
		$(form).attr('action',url).submit();
	}