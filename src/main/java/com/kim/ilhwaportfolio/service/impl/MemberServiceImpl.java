package com.kim.ilhwaportfolio.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kim.ilhwaportfolio.dto.Member;
import com.kim.ilhwaportfolio.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private SqlSession sqlsession;

	/**
	 * 1) 회원가입
	 * @return 데이터 insert(삽입) 후 업데이트 한 행의 개수
	 * @throws Exception
	 * @use join 컨트롤러
	 */
	@Override
	public int join(Member input) throws Exception {
		int result =0;
		result  = sqlsession.insert("MemberMapper.join", input);
		if (result == 0) {
			throw new NullPointerException("result is 0");
		}
		return result;
	}
	
	/**
	 * 2) 회원 로그인
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 * @use signIn 컨트롤러 , myPage 컨트롤러
	 */
	@Override
	public Member signIn(Member input) throws Exception {
		Member result = null;
		result = sqlsession.selectOne("MemberMapper.signIn", input);
		return result;
	}

	/**
	 * 3) 이메일 기억하기 
	 * @return 조회된 회원의 email(cookie에 저장되어있는 membernum에 매치되는 이메일을 가져온다)
	 * @throws Exception
	 * @use  signIn 컨트롤러 
	*/
	@Override
	public String getEmailCookie(int input) throws Exception {
		String result = "";
		result = sqlsession.selectOne("MemberMapper.getEmailCookie", input);
		if (result == null || result.equals("")) {
			throw new NullPointerException("result is null ");
		}
		return result;
	}

	/**
	 * 4) 회원 유무 조회
	 * @return 조회된 데이터 갯수 
	 * @throws Exception
	 * @use  emailExistCheck 컨트롤러 , 
	*/
	@Override
	public int emailExistCheck(Member input) throws Exception {
		int result = 0;
		result = sqlsession.selectOne("MemberMapper.emailExistCheck", input);
		return result;
	}
	
	/**
	 * 5) 이메일 찾기
	 * @return 조회된 데이터 ( EmailLIST )
	 * @throws Exception
	 * @use  findEmail 컨트롤러 
	*/
	@Override
	public List<String> findEmail(Member input) throws Exception{
		List<String> result = new ArrayList<>();
		result  = sqlsession.selectList("MemberMapper.findEmail", input);
		return result;
	}

	/**
	 * 6) 비밀번호 재 설정
	 * @return 수정된 데이터 갯수 
	 * @throws Exception
	 * @use resetPassoword 컨트롤러 , 마이페이지에서 비밀번호 변경 시
	*/
	@Override
	public int resetPassword(Member input) throws Exception {
		int result = 0;
		result = sqlsession.update("MemberMapper.resetPassword", input);
		if (result == 0) {
			throw new NullPointerException("result = 0");
		}
		return result;
	}

	/**
	 * 7) 회원 데이터 상세 조회
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 * @use mypage 컨트롤러 
	*/
	@Override
	public Member memberInfo(Member input) throws Exception {
		Member result = null;
		result = sqlsession.selectOne("MemberMapper.memberInfo", input);
		if (result == null) {
			throw new NullPointerException("result is null");
		}
		return result;
	}

	/**
	 * 8) 비밀번호 변경을 위한 현재 비밀번호 확인
	 * @return 조회된 데이터 수
	 * @throws Exception
	 * @use currentPasswordChk 컨트롤러 
	*/
	@Override
	public int currentPasswordChk(String input) throws Exception {
		int result = 0;
		result = sqlsession.selectOne("MemberMapper.currentPasswordChk", input);
		return result;
	}

	/**
	 * 9) 회원 정보 재 설정 
	 * @return 수정된 데이터 수
	 * @throws Exception
	 * @use modifyMemberInf 컨트롤러 
	*/
	@Override
	public int modifyMemberInfo(Member input) throws Exception {
		int result = 0 ;
		result = sqlsession.update("MemberMapper.modifyMemberInfo", input);
		if(result == 0) {
			throw new NullPointerException("result  = 0 ");
		}
		return result;
	}

	/**
	 * 10) 회원탈퇴
	 * @return 삭제된 데이터 수
	 * @throws Exception
	 * @use signoutMember 컨트롤러 
	*/
	@Override
	public int signout(Member input) throws Exception {
		int result = 0 ;
		result = sqlsession.delete("MemberMapper.signout", input);
		if(result == 0) {
			throw new NullPointerException("result = 0");
		}
		return result;
	}
}
