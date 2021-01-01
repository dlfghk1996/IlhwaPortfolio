package com.kim.ilhwaportfolio.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.kim.ilhwaportfolio.dto.Member;


@Component
public class WebHelper {

	// 기본 인코딩 타입 설정  
	private String encType;
	private int start;
	private int end;
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public String getEncType() {
		return encType;
	}
	private Map<String, Object> map;

	public void setEncType(String encType) {
		this.encType = encType;
	}
	public WebHelper() {
	
	}

	// JSP의 request 내장 객체 
	//private HttpServletRequest request;

	// JSP의 response 내장 객체 
	//private HttpServletResponse response;
	
	/** redirect -> 로그인 실패시 redirect하고 있음 */
	public void redirect(String msg) throws IOException {
		System.out.println("redirect 들어왔음");
		
		ServletRequestAttributes servletContainer  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletContainer.getRequest();
		HttpServletResponse response = servletContainer.getResponse();
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out;
		out = response.getWriter();
		out.println("<script>alert('"+ msg +"'); history.go(-1);</script>");
		out.flush();
	} 
	
	/** 세션값 가져오기 */
	/** 값이 없을 경우 */
	public Object getSession(String key,Object defaultValue ) {
		ServletRequestAttributes servletContainer  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletContainer.getRequest();
		HttpSession session = request.getSession();
		Object sessionValue =  (Member)session.getAttribute(key);
		if(sessionValue == null) {
			return null;
		}
		return sessionValue;
	}
	
	/** 페이지네이션  */
	public String pagenation(
			String pagename, 
			int nowPage, 
			int totalCount,
			int listCount,
			int pageCount
			) {
		  // MySQL의 Limit 시작 위치
		 // 검색 범위의 시작 위치
		 this.start = (nowPage - 1) * listCount+1;
		 this.end = nowPage * listCount;
		 StringBuffer sb =new StringBuffer();
		 // 페이지 네이션 총수
		 int totalPage = totalCount/listCount+1; // 홀수 일경우
	     if((totalCount%listCount)==0){listCount--;} //짝수일경우
	     // 현재 사용자의 위치의 그룹
		 int userGroup = nowPage/pageCount;
	     if(nowPage%pageCount==0){userGroup--;};
	     
	     // <a href="GoBorad.onepiece?cp=(userGroup-1)*pageSize+pageSize">이전</a>
	     sb.append("<ui class=pagination>");
	     if(userGroup != 0) {
	    	 sb.append("<li><a href='");
	    	 sb.append(pagename);
	    	 sb.append("?nowPage=");
	    	 int prev = (userGroup-1)*pageCount+pageCount;
	    	 sb.append(prev);
	    	 sb.append("'>이전</a></li>");
	     }
	     
	     // 보여줄 페이지네이션 구성
	     // &nbspt;&nbspt;<a href="GoBorad.onepiece?cp=i>">i</a>
	     for(int i=(userGroup*pageCount)+1; i<=(userGroup*pageCount)+pageCount; i++){
	    	  sb.append("<li><a href='");
	      	  sb.append(pagename);
	      	  sb.append("?nowPage=");
	      	  sb.append(i);
	      	  sb.append("'>");
	      	  sb.append(i);
	      	  sb.append("</a></li>");
	      	  if(i == totalPage){ break; }
	     }
	     
	     // next
	     // <a href="GoBorad.onepiece?cp=(userGroup+1)*pageSize+1">다음</a>
	     if(userGroup != ((totalPage/pageCount)-(totalPage%pageCount==0?1:0)) ) {
	    	 sb.append("<li><a href='");
	    	 sb.append(pagename);
	    	 sb.append("?nowPage=");
	    	 int next = (userGroup+1)*pageCount+1;
	    	 sb.append(next);
	    	 sb.append("'>다음</a></li>");
	     }
	     sb.append("</ui>");
		     return sb.toString();	
		}
	}

