package com.kim.ilhwaportfolio.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

/**
 * @ControllerAdvice로 케어 가능한 범위는 Dispatcher Servlet내
 * @RestControllerAdvice
 * @ControllerAdvice 어노테이션과  @ResponseBody 어노테이션을 합친것 !
 * 만약 예외처리를 body로 전달하고 싶다면 @RestControllerAdvice 이용하면 된다
   @ControllerAdvice와  @ResponseBody 를  어노테이션 기반 인터셉터 (annotation driven interceptor)로 이해
 */


	public GlobalDefaultExceptionHandler() {}
	
	/**
	 * 	db에서  데이터를 전달받지못함 
	 *  select 쿼리의 결과로  db에 일치하는 값이 없을 떄 발생. 
	 *  로그인, 이메일 중복검사, 이메일찾기 ,비밀번호 일치검사
	 *  로그인에서 오류가 발생했을 시 redirect처리
	*/ 
	// 
	@ExceptionHandler(CustomeException.class)
    public void CustomeException(CustomeException e)throws Exception{
		WebHelper webhelper = new WebHelper();
		String msg = "존재하는 회원이 없습니다.";
		webhelper.redirect(msg);
		
	}
	/**500에러
	 * ajax 통신
	 * select 쿼리의 결과로  db에 일치하는 값이 없을 떄 발생.
	 * insert,cookieGetEmail,비밀번호 재 설정,mypage,회원정보수정,회원탈퇴 
	 * */
	@ExceptionHandler(NullPointerException.class)
	public String nullPointerHandle(NullPointerException e, HttpServletRequest request) {
		  String ajaxHeader = request.getHeader("X-Requested-With");
          if(ajaxHeader != null && "XMLHttpRequest".equals(ajaxHeader)){
        	  jsonNullPointerHandle();
          }
          System.out.println("nullpoint" + e.getMessage());
		return "error/500error";
	}
	
	
	public @ResponseBody Map<String,Object> jsonNullPointerHandle() {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("[ ajax통신 중 error nullpoint ] " );
		map.put("result", "/");
		return map;
	}
	
	
	/**500에러
	 * query문 오타 등
	 * */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
	@ResponseBody
	public String runtimeException(RuntimeException e,HttpServletRequest request){
	
		String ajaxHeader = request.getHeader("X-Requested-With");
          if(ajaxHeader != null && "XMLHttpRequest".equals(ajaxHeader)){
        	  	jsonruntimeException();
          }
		System.out.println("[에러]  RuntimeException" + e.getLocalizedMessage()  + "ㅎㅎ");
		return "error/500error";
	}

	@ResponseBody
	public Map<String, Object> jsonruntimeException(){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("[ ajax통신 중 error RuntimeException ] " );
		map.put("result", "/");
		return map;
	}
	
	@ExceptionHandler(IOException.class)
    // . 문잔변환예외
    // . FileNotFoundException
    // . interuptedIoexception
    public String IOException(IOException ex) {
		 System.out.println(ex.getLocalizedMessage());
        return "redirect:index";
	    }
	}

