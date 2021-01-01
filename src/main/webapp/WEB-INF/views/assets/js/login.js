
//  bootsrap modal 에서 영역 밖을 선택했을 때 modal 이 닫히는 걸 방지 : backdrop: 'static'
//  bootsrap modal 에서 ESC 눌렀을 때 modal이 닫히는 걸 방지 : data-keyboard : false

// 모달 공통로직
// 1. 수동 닫기 버튼
// 2. 모달open시 입력값 초기화

// 이메일 찾기 modal close 및  html remove
$(document).on("click","#closebtn",function(){
	alert("닫기버튼을 누");
	$('#modal-find-result-email').modal('hide');
})  

// 전역 변수
	// 이메일 찾기 화면 바꾸는 변수
	var findemailform;
	// 중복검사 이후 id가 변경되었는지 확인하기 위한 변수
	var dupleCheckedID = "";
	
// 회원 가입	
function join(){
	// id 중복 검사 여부 확인
	if($('#emailDuplChkAction').val() !='y' || $('#email').val()!=dupleCheckedID){
		alert('이메일 중복 확인바랍니다.');
		$('#emailDuplChkAction').eq(0).focus();
		return false;
	}
    $.ajax({
        url: 'join',
        type: 'POST',
        data: $('#form-join').serialize(),
      	//서버가 보내는 데이터  Map<String, Object>
        success: function(response){
				if( response.result == '1'){
					alert('회원가입이 성공적으로 되었습니다.');
					location.href='index';
				}
				alert(response.result);
        },
        error: function(response){
			alert('[ajax 통신 error]');
        }
    })
}
	
// 아이디 중복확인
function emailDuplCheck(){
   	if($('#email').val() != ''){
	   	    $.ajax({
	        url: 'emailExistCheck',
	        type: 'POST',
	        data:{ email : $('#email').val()},
			success: function(response){
			console.log(response); // json형식인지  다시 확인 
	        	if(response.result == 0){
	        		var result=confirm( '사용가능한 이메일 입니다.\n사용하시겠습니까?');
					if (result) {
						dupleCheckedID = $('#email').val();
						$('#emailDuplChkAction').val('y');
					} else {
						alert("이메일을 다시 입력해주세요");
						$('#emailDuplChkAction').val('n');
					}
				}else{
	        		alert("이미 사용중인 이메일 입니다.");
	        		$('#emailDuplChkAction').val('n');
	        	}
			},
	        error: function(response){
	        	alert('[ajax 통신  error]');
	        }
	        
	    	})
   	}else{
   		alert("이메일을 입력해주세요");
   	}
}   

// 이메일 찾기
function findEmail(){
	  $.ajax({
		  url: 'findEmail',
		  data : $('#form-find-email').serialize(),
          type : 'POST',
          success: function(response){
          //each() 메서드는 매개 변수로 받은 것을 사용해 반복문과 같이 배열이나 객체의 요소를 검사한다.
         	if(response.result == null || response.result.length == 0){
				alert('존재하는 이메일이 없습니다.');
			}else{
          	 	$.each(response.result, function(i){
          	 		$('#findEmailList').append('<tr><td>' + response.result[i] + '</td>');
				});
				$('#modal-find-email').modal('hide');
          		$('#modal-find-result-email').modal('show');
          	}
		  },
          error: function(){
            	alert('[ajax 통신  error]');
			}
	 })
}

// 비밀번호 변경 : 인증번호 전송
function sendVerificationCode(){
	$.ajax({
	  url: 'sendVerificationCode',
	  data :  $('#form-find-pw').serialize(),
      type : 'POST',
      success: function(response){
      	if(response.result > 0){
      		alert('입력하신 정보로 인증번호를 보내드렸습니다.');
      		$('#receiveVerificationCode').val('y');
      	}else{
			alert('회원정보 확인을 다시 해주세요.');
			}
	  },
	  error: function(response){
        	 alert('[ajax 통신  error]');
        	 }
     })
 }

// 비밀번호 변경 : 입력값 최종확인
function finalValueCheckForPwReset(){
	// 인증 코드를 발급 받았으면 메소드 실행
	if( $('#receiveVerificationCode').val() != ''){
		  $.ajax({
			  url: 'finalValueCheckForPwReset',
			  data :  $('#form-find-pw').serialize(),
	          type : 'POST',
	          success: function(response){
	        	  if(response.result== 'success'){
	        		  alert("인증번호가 확인되었습니다. 비밀번호 변경페이지로 넘어갑니다.");
	        		  $('#modal-find-pw').modal('hide');
	        		  $('#modal-reset-password').modal('show');
	        	  }else{
	        		  alert('회원정보 및 인증번호가 일치하지 않습니다. 회원님의 정보보호를 위해 다시한번 확인해주세요');
	        	  }
			},
	          error: function(message){
	        	  alert('[ajax 통신  error]');
			}
		 })
	}else{
		alert("자세한 확인을 위해 회원정보 확인 및 인증번호를 입력해주세요")
	}
}
// 비밀번호 변경 : 유효성검사
function resetPassword(){
	if($('#newPassword').val() == $('#confirmPassword').val()){
		  $.ajax({
			  url  : 'resetPassoword',
			  data :  {password : $('#newPassword').val()},
	          type : 'POST',
	          success: function(response){
	        	  var message  = response.result == -1?"비밀번호는 영문,숫자,특수기호 포함되어야 합니다.":response.result ==1?"비밀번호가 변경되었습니다.":"비밀번호 변경에 실패하였습니다.";
	        	  alert(message);
	        	  if(response.result == 1 ){location.reload();}
			  },
	          error: function(response){
	        	  alert('[ajax 통신  error]');
			}
		 })
	}else{
		alert('비밀번호가 일치하지않습니다.');
	}
}
