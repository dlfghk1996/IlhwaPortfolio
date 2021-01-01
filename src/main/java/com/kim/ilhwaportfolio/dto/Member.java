package com.kim.ilhwaportfolio.dto;

import java.util.Date;

import org.springframework.stereotype.Repository;

@Repository
public class Member {
	
	private int membernum;
	private String email;
	private String password;
	private String name;
	private String gender;
	private String birthdate;
	private String motto;
	private String regdate;


		// 이메일 기억하기 쿠키
	private boolean emailRememberChecked;

	public int getMembernum() {
		return membernum;
	}

	public void setMembernum(int membernum) {
		this.membernum = membernum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public boolean getEmailRememberChecked() {
		return emailRememberChecked;
	}

	public void setEmailRememberChecked(boolean emailRememberChecked) {
		this.emailRememberChecked = emailRememberChecked;
	}

	@Override
	public String toString() {
		return "Member [membernum=" + membernum + ", email=" + email + ", password=" + password + ", name=" + name
				+ ", gender=" + gender + ", birthdate=" + birthdate +"]";
		}
	}

