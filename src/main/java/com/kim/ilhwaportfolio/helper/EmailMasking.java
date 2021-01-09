package com.kim.ilhwaportfolio.helper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailMasking {
	// 일화 방법
	// int domain = email.indexOf('@'); // 1 // 맨 처음값의 위치를 찾음
	// String emailWithoutDomain = email.substring(0,domain);
	// emailWithoutDomain.length;

	public List<String> getMaskedEmail(List<String> finedEmailList) {
		List<String> email = new ArrayList<String>();
		String regex = "\\b(\\S+)+@(\\S+.\\S+)";

		   // 마스킹 처리할 부분인 emailId 추출
		   // 방법: 그룹을 이용한 추출: 정규식 패턴에서    "()" 를 기준으로 그룹화한다.  (1그룹)(2그룹)(3그룹)
		   // String emailWithoutDomain = matcher.group(1); 
			
			  /*
			  * "emailWithoutDomain" 의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
			  * 세글자인 경우 뒤 두글자만 마스킹,
			  * 세글자 미만인 경우 모두 마스킹 처리
			  */
		      String emails="";
		      for(int i=0; i<finedEmailList.size(); i++) {
		    	  // ["dlfghk1996@naver.com","dlfghk@naver.com"]
		    	  // 일치하는 문자열을 추출할 때 group()함수를 쓰려면,
		    	  // find() 메소드를 사용해야 커서가 이동하므로 반드시 꼭 써준다
		    	  Matcher matcher = Pattern.compile(regex).matcher(finedEmailList.get(i));
		    	  if (matcher.find()) {
		    		  String emailWithoutDomain = matcher.group(1); 
		    		  int length = emailWithoutDomain.length();
				      if (length < 4) {
				    	  char[] c = new char[length];
				    	  Arrays.fill(c, '*');  // [*,*,*]
				    	  emails = finedEmailList.get(i).replace(emailWithoutDomain, String.valueOf(c));
				      } else if (length == 4) {
				    	  emails = finedEmailList.get(i).replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
				      } else {
				    	  emails = finedEmailList.get(i).replaceAll("\\b(\\S+)[^@][^@][^@][^@]+@(\\S+)", "$1****@$2");
				      	}
					      email.add(emails);
				}
		    }
			 return email;
		}
	}

