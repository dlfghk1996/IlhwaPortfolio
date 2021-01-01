package com.kim.ilhwaportfolio.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kim.ilhwaportfolio.dto.Member;
import com.kim.ilhwaportfolio.helper.CustomeException;
import com.kim.ilhwaportfolio.helper.EmailMasking;
import com.kim.ilhwaportfolio.helper.MailSend;
import com.kim.ilhwaportfolio.helper.MemberValidator;
import com.kim.ilhwaportfolio.helper.WebHelper;
import com.kim.ilhwaportfolio.service.MemberService;
@Controller
public class ViewController {
	
		@Autowired
		MessageSource messagesource;
		@Autowired
		MemberService memberService;
		@Autowired
		WebHelper webhelper;
		@Autowired
		Member member;
		
		/** 메인 페이지  */
		@RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
		public String index() {
			return "index";
		}

		/** 로그인 페이지  */
		// @CookieValue는 어떤 매개변수가 HTTP Cookie로 매핑될 것인지 명시하는 annotation이다
		@RequestMapping(value = "login", method = RequestMethod.GET)
		public String login(Model model, @CookieValue(value = "emailRememberChecked", required = false) Cookie cookie)throws Exception {
			// 이메일을 기억하겠다는 value가 쿠키에 저장되어있다면, 사용자의 이메일을 가져온다.
			// Cookie가 null이 아니면 member의 멤버변수를 Cookie에 있는 내용으로 바꾸는 것
			String email = "";
			if (cookie != null && cookie.getValue() != "") {
				email = memberService.getEmailCookie(Integer.parseInt(cookie.getValue()));
				model.addAttribute("userEmailInCookie", email);
			}
			return "login";
		}
		
		/** mypage  */
		@RequestMapping(value = "myPage",method = RequestMethod.GET)
		public String mypage(HttpSession session, Model model) throws Exception{
			// 현재 세션에 있는 아이디를 대상으로 정보 select
			member = memberService.memberInfo((Member)session.getAttribute("members"));
			model.addAttribute("member", member);
			return "mypage";  
		}
		//----------------------------------------------------------------------------------
		/** 1. 로그인 실행 */
		// 로그인은 웹 페이지 사용 시 지속적으로 필요한 정보들을 session에 저장한다.
		@RequestMapping(value = "signIn", method = RequestMethod.POST)
		public String signIn(Member member, HttpSession session,HttpServletResponse response) throws Exception {

			// session에 기존의 사용자 정보가 남아있다면 기존 value을 제거해준다.
			if (session.getAttribute("email") != null || session.getAttribute("name") != null) {
				/**
				 * invalidate 메소드로  session안에 데이터를 일괄삭제 하지 않는 이유
				 * session안에 저장되어있는 데이터는 로그인만을  위한것이 아니기 때문에 , 필요없는 정보만 삭제한다.
				 * **/
				session.removeAttribute("email");
				session.removeAttribute("name");
			}
				response.setContentType("text/html; charset=utf-8");

				/** 변수에 담는 이유 
				*   원인 : 쿼리문 실행 시 meberTable에  이메일 기억하기 관련 컬럼이 없기 때문에  쿼리문 실행 결과 로 member객체를 꺼내올 때  EmailRememberChecked value가 초기화된다.
				*   해결 : checkbox에 db에서 반환되기 전 초기 EmailRememberChecked 값을 할당해둔다. 
				**/ 
				boolean emailRememberChecked = member.getEmailRememberChecked();
				// 로그인 쿼리문 실행.
				member = memberService.signIn(member);
				if(member == null) {
					System.out.println("존재하는 회원 없음");
					throw new CustomeException();
				}
				
				// 로그인 기능 실행 
				session.setAttribute("members",member);

				// EmailRememberChecked 여부 검사.
				if (emailRememberChecked) {
					Cookie newCookie = new Cookie("emailRememberChecked", Integer.toString(member.getMembernum()));
					newCookie.setPath("/");
					newCookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유지기간 : 한 달
					response.addCookie(newCookie);
				} else {
					Cookie deleteCookie = new Cookie("emailRememberChecked", null);
					deleteCookie.setPath("/");
					deleteCookie.setMaxAge(0);
					response.addCookie(deleteCookie);
				}

			return "index";
		}

		
		/** 2. 로그아웃 실행  */
		@RequestMapping(value = "logOut")
		public String logOut(HttpServletRequest request, HttpServletResponse response) throws Exception{
			request.getSession().invalidate(); // 새로운 새션ID를 생성하므로써 session을 초기화 한다.
			return "index";
		}

		
		/** 3. 회원가입
		    사용자가 입력한 값이 join에 자동으로 매핑됩니다. 여기에 @Valid 아노테이션을 지정하면 유효성 검사가 이루어지고 , 그결과는 bindingResult게 담깁니다. 
		 */
		/** ajax 통신에서 쓰는 어노테이션 
		 * @ResponseBody와 @RequestBody
		 * 프론트에서 자바에게 JSON형태로 전달해줄때 :  @RequestBody
		 * JSON형태로 프론트단에 전달해야 하는 경우:   @ResponseBody
		 * 클라이언트쪽에서 데이터 형태를 JSON으로 보내주면, 서버쪽에서도 JSON으로 받아줘야한다.
		 * 
		 * */
		@RequestMapping(value = "join", method = RequestMethod.POST)
		public @ResponseBody Map<String, Object> join(@Valid Member member, BindingResult bindingResult,Locale locale) throws Exception{
			new MemberValidator().validate(member, bindingResult);
			String errormessage = null;
			// map은 순서가 없기 때문에, 순서 대로 담아주는 LinkedHashMap을쓴다.
			// map의 value타입이 object인 이유: 당사자 빼고는 beans의 데이터타입을 알지 못하기 때문에 object로 통일한다.
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			List<FieldError> errors = bindingResult.getFieldErrors();
			
			if (bindingResult.hasErrors()) {
				System.out.println("회원가입 에러발생");
				for (int i = 0; i< errors.size(); i++) {
					FieldError error = errors.get(i);
					errormessage = messagesource.getMessage(error.getCode(),null,Locale.getDefault());
					map.put("result", errormessage);
					System.out.println(errormessage);
					return map;
				}
			}
			// 에러가 없을경우 회원가입 실행
			int result;
			result = memberService.join(member);
			map.put("result", result);
			return map;
		}

		/** 4. 회원 여부 확인 */
		@RequestMapping(value = "emailExistCheck", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object> emailExistCheck(Member member) throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			int result = 0;
			result = memberService.emailExistCheck(member);
			map.put("result", result);
			return map;
		}
	//---------------------------------------------------------------------------------------------	
		/** 5.이메일 찾기 */
		@RequestMapping(value = "findEmail", method = RequestMethod.POST)
		public @ResponseBody Map<String, List<String>> findEmail(Member member) throws Exception{
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			List<String> finedEmailList = new ArrayList<>();
			EmailMasking em = new EmailMasking();
			finedEmailList = em.getMaskedEmail( memberService.findEmail(member));
			map.put("result", finedEmailList);
			return map;
		}
		
		/** 6.비밀번호 변경 -> (인증번호 전송) */
		@RequestMapping(value = "sendVerificationCode", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object> sendVerificationCode(Member member, @RequestParam("recipient") String recipient, HttpServletRequest request,HttpSession session)throws Exception{
			String verificationCode ="";
			Map<String, Object> map = new HashMap<String, Object>();
			

			int countOfUser = memberService.emailExistCheck(member);
			//DB에 있는 회원 이라면 인증번호를 발급하고, 사용자가 입력한 정보를 세션에 저장한다.
			if(countOfUser>0) {
				// 비밀번호 찾기 인증번호 발급
				UUID uuid = UUID.randomUUID();
				// UUID 값의 대시(-) 제거 및 5글자로 자르기
				verificationCode = uuid.toString().replaceAll("-", "").substring(0,5); 
				// 사용자 이메일로 임시비밀번호 전송
				new MailSend(recipient,verificationCode);
				// 세션에 사용자 정보 저장
				session.setAttribute("member",member);
				session.setAttribute("verificationCode", verificationCode);
				session.setAttribute("recipient", recipient);
				String sessionTest = member.toString();
				System.out.println(sessionTest);
				map.put("result",countOfUser);
			}
			return map;
		}

		
		/** 6-1. 비밀번호 변경 -> ( 임시비밀번호와 사용자입력값 일치 비교 )
	    // 임시비밀번호를 맞게 입력 했다면 : 비밀번호 변경 페이지로 넘어간다.
		// 일치하지않는다면   ALERT("잘못된 인증번호 입니다.")
		 */
		@RequestMapping(value = "finalValueCheckForPwReset", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object> finalValueCheckForPwReset(HttpServletRequest request, HttpSession session,
				@RequestParam("email") String email,
				@RequestParam("name") String name,
				@RequestParam("verificationCode") String verificationCode
				)throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			
			Member member =  (Member)session.getAttribute("member");
			System.out.println(member.toString());
			String semail =  member.getEmail();
			String sname =  member.getName();
			String sverificationCode = (String)session.getAttribute("verificationCode");
			String result = email.equals(semail) && name.equals(sname) && verificationCode.equals(sverificationCode)?"success":"false";
			map.put("result",result);
			return map;
		}
		
		 /** 7. 새로운 비밀번호 유효성 검사 및 update */
		@RequestMapping(value = "resetPassoword", method = RequestMethod.POST ,produces = "application/json; charset=utf8")
		public @ResponseBody Map<String,Object> resetPassoword(@RequestParam("password") String password, HttpServletRequest request,HttpSession session)throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			int result=0;
			String pw_regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[()$@$!%*#?&]).*$";
			boolean pw_check = Pattern.matches(pw_regex, password);
			// if true 일경우 : 패턴 안맞음
			if (!(pw_check) || password.length() > 15 || password.length() < 5) {
				result = -1;
			}else {
				Member member = (Member)session.getAttribute("member");
				member.setPassword(password);
				result = memberService.resetPassword(member);
				System.out.println(1);
				session.invalidate();
			}
			map.put("result",result);
			return map;
		}
		
		/** 8. mypage 비밀번호 변경을 위한 현재 비밀번호 확인  */
		@RequestMapping(value = "currentPasswordChk", method = RequestMethod.POST ,produces = "application/json; charset=utf8")
		public @ResponseBody Map<String,Object>  currentPasswordChk(@RequestParam String currentPassword)throws Exception {
			Map<String, Object> map = new HashMap<String, Object>();
			int result = memberService.currentPasswordChk(currentPassword);
			map.put("result",result);
			return map;
		}
		
		/** 9. 전체변경 */
		@RequestMapping(value = "modifyMemberInf",method = RequestMethod.POST)
		public @ResponseBody Map<String,Object> modifyMemberInfo(Member member) throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			int result = memberService.modifyMemberInfo(member);
			//if(result == 0) {
			//	throw new CustomeException();
			//}
			return map;
		}
		
		/** 10. 회원탈퇴 */
		@RequestMapping(value = "signout", method = RequestMethod.POST)
		public String signoutMember(Member member , HttpSession session, Model model)throws Exception {
			Member sessionMember = (Member)session.getAttribute("members");
			
			String oldPass = sessionMember.getPassword();
			String newPass = member.getPassword();

			if(oldPass.equals(newPass)) {
				memberService.signout(sessionMember);
				session.invalidate();
				return "redirect:/";
			}else {
				throw new CustomeException();
			}
		}

	}

