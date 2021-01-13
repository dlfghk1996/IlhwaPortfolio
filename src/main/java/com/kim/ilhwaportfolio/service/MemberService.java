package com.kim.ilhwaportfolio.service;

import java.util.List;

import com.kim.ilhwaportfolio.dto.Member;

public interface MemberService {
	// join
	public int join(Member input) throws Exception;
	
	// signIn
	public Member signIn(Member input) throws Exception;
	
	// RememberEmail in cookie
	public String getEmailCookie(int input) throws Exception;
	
	// emailExistCheck (sign up)
	public int emailExistCheck(Member input) throws Exception;
	
	// findEmail
	public List<String> findEmail(Member input) throws Exception;

	// resetPassword
	public int resetPassword(Member input) throws Exception;

	// memberInfo
	public Member memberInfo(Member input) throws Exception;
	
	// memberInfo current Password Check
	public int currentPasswordChk(String input) throws Exception;
	
	// modify memberInfo 
	public int modifyMemberInfo(Member input) throws Exception;
	
	// signOut
	public int signout(Member input) throws Exception;
}
