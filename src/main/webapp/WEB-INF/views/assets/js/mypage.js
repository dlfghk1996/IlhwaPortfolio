/**
 * 
 */
// mypage : 현재비밀번호 검사


$('#currentPasswordChk').click(function(){
	if($('#currentPassword').val() != ''){
		$.ajax({
			url: 'currentPasswordChk',
			type: 'POST',
			data: {'currentPassword' : $('#currentPassword').val()},
			success: function(response){
				var msg = response.result>0?'비밀번호가 확인되었습니다':'비밀번호가 잘못되었습니다.';
				if( response.result>0){
					$('#passwordChkok').val('y');
				}
				alert(msg);
			},
			error: function(){
				//alert('[ ajax통신 에러 ]');
			}
		});
	}else{alert('현재 비밀번호를 입력해주세요.');}
});

// mypage : 비밀번호 변경유효성검사
$('#changePasswordBtn').click(function(){
		var pwChkok = $('#passwordChkok').val();   // 기존비밀번호 검사 여부
		var password = $('#password').val(); // 새로운비밀번호
		var newPwCk = $('#changePassword').val();  // 새로운비밀번호 확인
		if(pwChkok == "" || pwChkok == null){ 
			alert(" 사용자 정보 보호를 위해 비밀번호를  확인해주세요.");
		}else{
			if(newPw != newPwCk){
    			alert('비밀번호가 일치하지 않습니다.');
    			  $('#changePasswordChk').focus();
    		}else{
	    		$.ajax({
	    			url: 'resetPassoword',
	    			type: 'POST',
	    			data:  password,
	    			success: function(response){
	  	        	  	var message  = response.result == -1?'비밀번호는 영문,숫자,특수기호 포함되어야 합니다.':response.result ==1?'비밀번호가 변경되었습니다.':'비밀번호 변경에 실패하였습니다.';
	  	        	  	if( response != '1'){$('#changePasswordBtn').val('n');}
		        	  	alert(message);
	    			},
	    			error: function(){}
	    		});
    		}
		}
});

//  mypage : 회원정보수정
$('#modifyMemberInfo').click(function(){
alert('회원정보수정');
	// $('#changePasswordBtn') : 비밀번호 유효성 검사 처리가 'n'이거나
	// $('#changePasswordChk') : 새로운비밀번호확인 input이 빈값이 아닐경우
	// $('#password').val() : 새로운비밀번호 input이 빈값이 아닐경우 
	if($('#changePasswordBtn').val() == 'n' || $('#changePasswordChk').val() !="" || $('#password').val() !="" ){ alert("비밀번호 변경을 완료해주세요"); return;}
	$.ajax({
		url: 'modifyMemberInf',
		type: 'POST',
		data: $('#form-memberinfo').serialize(),
		dataType: 'JSON',
		success: function(response){
			alert("회원 정보가 수정되었습니다.");
		},
		error: function(response){
			alert(response.result);
		}
	});
});