
// madal 닫힐 때 form > input 초기화
$('.modal').on('hidden.bs.modal', function(e){
  $(this).find('form')[0].reset()
}); 

// 로그인
$(function(){
  $.validator.addMethod("noSpace", function(value, element) {
		return value.indexOf(" ") < 0 && value != ""; 
		}, "공백을 입력하실수 없습니다.");
	$('#form-login').validate({
		rules:{
			email:{
				required : true, //필수입력여부
				email : true, 	//이메일형식
				noSpace: true
			},
			password:{
				required : true, //필수입력여부
				noSpace: true
			},
		},
		messages:{
			email:{
				required : '이메일은 필수 입력입니다.',
				email : '잘못된 형식 입니다.'
			},
			password: {
				required : '비밀번호는 필수 입력입니다.',
			},
		}
	})
});

// 이메일 찾기 화면 바꾸는 변수
var findemailform;
// 중복검사 이후 id가 변경되었는지 확인하기 위한 변수
var dupleCheckedID = "";
	
// 회원가입	
function join(){

	// id 중복 검사 여부 확인
	if($('#emailDuplChkAction').val() != 'y' || $('#email').val() != dupleCheckedID){
		alert('이메일 중복 검사가 필요합니다.');
		$('#emailDuplChkAction').eq(0).focus();
		return false;
	}
	
	// 비밀 번호 일치 체크 확인
	if($('#password').val() != $('#passwordChk').val()){
		alert('비밀번호가 일치하지않습니다.');
		$('#passwordChk').eq(0).focus();
		return false;
	}
	
	// 동의 체크 확인
	if(!$('#joinAgree').is(":checked")){
		alert('약관에동의해 주세요');
		return false;
	}
    
	$.ajax({
        url : 'join',
        type : 'POST',
        data : $('#form-join').serialize(),
      	//서버가 보내는 데이터  Map<String, Object>
        success: function(response){
				if(response.result == '1'){
					alert('회원가입이 완료되었습니다.');
					location.reload();
				}
				alert(response.result);
        },
        error: function(response){
			alert('[ajax 통신 error]');
        }
    })
}
	
// 아이디 중복검사
function emailDuplCheck(){
   	if($('#email').val() != ''){
	   	    $.ajax({
	        url : 'emailExistCheck',
	        type : 'POST',
	        data : {email : $('#email').val()},
			success: function(response){
	        	if(response.result == 0){
	        		var result=confirm( '사용 가능한 이메일입니다.\n사용하시겠습니까?');
					if (result) {
						dupleCheckedID = $('#email').val();
						$('#emailDuplChkAction').val('y');
					} else {
						alert("이메일을 다시 입력해주세요");
						$('#emailDuplChkAction').val('n');
					}
				}else{
	        		alert("이미 사용중인 이메일입니다.");
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
		  url : 'findEmail',
		  data : $('#form-find-email').serialize(),
          type : 'POST',
          success: function(response){
			//each() 메서드는 매개 변수로 받은 것을 사용해 반복문과 같이 배열이나 객체의 요소를 검사한다.
         	if(response.result == null || response.result.length == 0){
				alert('존재하지 않는 회원입니다.');
			}else{
				$('#modal-find-email').modal('hide');
				$("#findEmailList").children("div").remove();
          		$('#modal-find-result-email').modal('show');
				$('#findEmailList').append('<div>' + response.result.length + ' 개의 이메일이 등록되어 있습니다.</div>');
          	 	$.each(response.result, function(i){
          	 		$('#findEmailList').append('<div class="IH-emailList"> - ' + response.result[i] + '</div>');
				});
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
	  url : 'sendVerificationCode',
	  data : $('#form-find-pw').serialize(),
      type : 'POST',
      success : function(response){
      	if(response.result > 0){
      		alert('입력하신 이메일로 인증번호가 전송되었습니다.');
      		$('#receiveVerificationCode').val('y');
      	}else{
			alert('존재하지 않는 회원입니다.');
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
			  url : 'finalValueCheckForPwReset',
			  data : $('#form-find-pw').serialize(),
	          type : 'POST',
	          success : function(response){
	        	  if(response.result== 'success'){
	        		  alert("인증되었습니다. 새 비밀번호를 입력해 주세요.");
	        		  $('#modal-find-pw').modal('hide');
	        		  $('#modal-reset-password').modal('show');
	        	  }else{
	        		  alert('회원정보 및 인증번호가 일치하지 않습니다.');
	        	  }
			},
	          error: function(message){
	        	  alert('[ajax 통신  error]');
			}
		 })
	}else{
		alert("회원정보 및 인증번호를 입력해 주세요.")
	}
}

// 비밀번호 변경 : 유효성검사
function resetPassword(){
	if($('#newPassword').val() == $('#confirmPassword').val()){
		  $.ajax({
			  url : 'resetPassoword',
			  data : {password : $('#newPassword').val()},
	          type : 'POST',
	          success: function(response){
	        	  var message  = response.result == -1 ? "비밀번호는 영문, 숫자, 특수기호 포함되어야 합니다." : response.result == 1 ? "비밀번호가 변경되었습니다." : "비밀번호 변경에 실패하였습니다.";
	        	  alert(message);
	        	  if(response.result == 1 ){location.reload();}
			  },
	          error: function(response){
	        	  alert('[ajax 통신  error]');
			}
		 })
	}else{
		alert('비밀번호가 일치하지 않습니다.');
	}
}