package com.kim.ilhwaportfolio.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.kim.ilhwaportfolio.dto.Member;

@Component
public class MemberValidator implements Validator{
	
		@Override
		//supports(Class): 매개변수로 전달된 클래스를 검증할 수 있는지 여부를 반환
	public boolean supports(Class<?> clazz) {
		
		return Member.class.isAssignableFrom(clazz);
	}

	// ■ ValidationUtils.rejectIfEmptyOrWhitespace() : 값:null, 길이 0, 값: 공백 문자

	// 첫번째 인자: 오류(Errors errors)를 담아주고,
	// 2 번째 인자: 필드 이름
	// 3 번째 인자: message.properties에 적었던 이름
	// 4 번째 인자: 만약 3 번째 인자의 값을 못 찾을 경우 메시지로 메시지(default message)

	//  ■ Properties 파일을 읽어오는 방법
	/*  
	 *   1. BindingResult에는 모델의 바인딩 작업중에 발생한 오류정보가 담긴다.**/
	//  ■ MessageSource :  errormessage 가 선언된 properties 파일을 읽어온다.
	//  ■ MessageSource구현 방법  : StaticMessageSource: 코드로 메시지를 등록한다.
	//                         :  ResourceBundleMessageSource: 리소스 파일로부터 메시지를 읽어와 등록한다.
	
	
	@Override
	//validate(Object,Errors): 매개변수로 전달된 객체를 검증하고 실패하면 Errors 객체에 에러를 등록한다.
	public void validate(Object target, Errors errors) {
		try {
			Member member = (Member) target;
			/**
			 * 방법 1 : Pattern 객체의 mather 메소드를 통해 얻어지는 Matcher 객체 사용 
			 * 1-1 패턴 만들기: Pattern
			 * pattern = Pattern.compile("^[a-zA-Z]*$"); 
			 * 1-2 패턴 일치 확인 : Matcher 
			 * Matcher matcher = pattern.matcher(member.getUseremail());
			 **/
			/**
			 * 방법 2 : java.util.rege.Pattern 클래스의 matches()메소드 사용 1-1 : 패턴 , 일치 한번에 진행
			 * boolean name_check = Pattern.matches("^[가-힣]*$", member.getUseremail());
			 **/
	//이메일 
			ValidationUtils.rejectIfEmptyOrWhitespace(errors,"email", "NotBlank.member.email");
			String email_regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

			boolean email_check = Pattern.matches(email_regex, member.getEmail());
			// if true 일경우 : 패턴 안맞음
			if (!(email_check)) {
				errors.rejectValue("email", "Pattern.member.email");
			}
	//비밀번호
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank.member.password");
			String pw_regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[()$@$!%*#?&]).*$";
			boolean pw_check = Pattern.matches(pw_regex, member.getPassword());
			// if true 일경우 : 패턴 안맞음
			if (!(pw_check) || member.getPassword().length() > 15 || member.getPassword().length() < 5) {
			errors.rejectValue("password", "Pattern.member.password", "passwordError");
			}
	//비밀번호 확인체크
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userpwCK", "NotBlank.member.userpwCK","비밀번호를 한번 더 입력해주세요!!");
			// if true 일경우 : 비밀번호 일치하지않음
			//if (!(member.isPwEqualToCheckPw())) {
			//	member.toString();
			//	errors.rejectValue("userpwCK", "noMatch", "Notmatch.member.userpwCK","비밀번호 일치 오류");

			//}

	//이름체크
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotBlank.member.name");
			String name_regex = "^[가-힣]*$";
			boolean name_check = Pattern.matches(name_regex, member.getName());
			// if true 일경우 : 패턴 안맞음
			if (!(name_check) || member.getName().length() < 2) {
				System.out.println("이름 에러" + member.getName());
				errors.rejectValue("name", "Pattern.member.name");
			}
	//생년월일 체크(생년월일 형식)
			//date - > String casting
			System.out.println(member.getBirthdate());
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthdate", "NotBlank.member.birthdate");
			//String bith_regex = "^((19|20)\\d\\d)?([- /.])?(0[1-9]|1[012])([- /.])?(0[1-9]|[12][0-9]|3[01])$";
			//boolean birth_check = Pattern.matches(bith_regex, birthdate);
			//System.out.println("생년월일 에러" + member.getBirthdate());
			// if true 일경우 : 패턴 안맞음
			//if (!(birth_check)) {
			//	errors.rejectValue("birthdate", "Pattern.member.birthdate");
			//}
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
  }
}

