$(document).ready(function() {
	
// summernote img upload
//var summernoteChk = $('.content').val();
//if(summernoteChk != null){}
	$('.content').summernote({
    	lang: 'ko-KR',
    	height: 500,
    	minHeight:null,
    	maxHeight: null,
    	focus: true,
    	placeholder: '글을 입력하세요',
    	// onImageUpload callback함수를 정의하여, 이미지 파일을 서버에 저장하고, 이미지를 호출 할 수 있는 URL을 리턴 받아서 입력하면, 이미지가 삽입된 것 처럼 보이게 됩니다.
    	// 이미지를 특정 경로에 업로드 후 고유한 url를 리턴하는 방식으로 구현한다.이 과정에서 url을 통한 외부 리소스 접근을 위한 톰캣 설정도 해줘야 한다.
    	callbacks: {//여기 부분이 이미지를 첨부하는 부분
				onImageUpload : function(files, editor, welEditable) {
						for(var i = files.length - 1; i >= 0; i--) {
                        uploadSummernoteImageFile(files[i], this);
                       }
               },
    	  //onPaste 함수(복붙에 대한 콜백)
    	  onPaste: function (e) {
				var clipboardData = e.originalEvent.clipboardData;
				if (clipboardData && clipboardData.items && clipboardData.items.length) {
					var item = clipboardData.items[0];
					if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
						e.preventDefault();
					}
				}
			}
        }
    });
     
// summernote img upload
	function uploadSummernoteImageFile(file, el) {
	var form_data  = new FormData();
		form_data.append("uploadfile", file);
		$.ajax({
			data : form_data,
			type : "POST",
			url : "imgUpload",
			contentType : false,
			processData : false,
			enctype: 'multipart/form-data',
			success : function(data) {
			    $(el).summernote('editor.insertImage', data);
				//파일 전송이 완료 되었을 경우, 이미지 파일의 url을 리턴 받을 것이고, summernote의 'editor.insertImage' 기능을 통해 이미지를 삽입 될 수 있도록 합니다.
			}
		});
	}
	
// 다중파일 업로드
    $('#boardForm input[name=file]').MultiFile({
        max: 5, //업로드 최대 파일 갯수 (지정하지 않으면 무한대)
        maxfile: 1024, //각 파일 최대 업로드 크기
        maxsize: 3024,  //전체 파일 최대 업로드 크기
        STRING: { //Multi-lingual support : 메시지 수정 가능
            remove : "제거", //추가한 파일 제거 문구, 이미태그를 사용하면 이미지사용가능
            duplicate : "$file 은 이미 선택된 파일입니다.", 
            selected:'$file 을 선택했습니다.', 
            toomuch: "업로드할 수 있는 최대크기를 초과하였습니다.($size)", 
            toomany: "업로드할 수 있는 최대 갯수는 $max개 입니다.",
            toobig: "$file 은 크기가 매우 큽니다. (max $size)"
        },
        list:"#uploadList" //파일목록을 출력할 요소 지정가능
    });
    
// 첨부 파일 삭제
	$(document).on('click', '.fileDeleteBtn', function(){
		$.ajax({
            type: 'post',
            url: 'attachFileDelete',
		    data: {
		            filenum: $(this).attr('data-value'), 
		            filepath: $(this).attr('data-value2')
		            },        
  			success: function(result){
  			   		$(this).parent().css('display','none');
  			   		$(this).parent().remove();

			},
			error : function(request, status, error) {
					var val = request.responseText;
       				alert("error"+val);
    		}
		});
	});
	


// 비회원 비밀번호 확인
	$('.button-nonMember-PwChk').click(function(){
   // 아무값없이 띄어쓰기만 있을 때도 빈 값으로 체크되도록 trim() 함수 호출
       if ($('#pwd').val().trim() == '') {
        alert('비밀번호를 입력해주세요');
        $('#pwd').focus();
    	}else{
    		$('#form-nonMember-PwChk').submit();
    	}
  	});
 });