package com.kim.ilhwaportfolio.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kim.ilhwaportfolio.dto.Board;
import com.kim.ilhwaportfolio.dto.BoardFile;
import com.kim.ilhwaportfolio.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {

	// 게시물 등록
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public int postContent(Board input) throws Exception{
		int result = 0;
		result = sqlSession.insert("BoardMapper.postContent",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}
	
	//  게시글 전체 출력
	@Override
	public List<Board> contentList(Board input) throws Exception {
		List<Board> result = null;
		System.out.println(input.getSearch() + " impl ");
		result = sqlSession.selectList("BoardMapper.contentList",input);
		return result;
	}
	
	// 이전글 다음글
	@Override
	public Map<String,Object> prevNext(int input) throws Exception {
		Map<String,Object> result = null;
		result = sqlSession.selectOne("BoardMapper.prevNext",input);
		return result;
	}

	// 게시글 상세보기
	@Override
	public Board viewContent(int input) throws Exception {
		Board result=null; 
		result = sqlSession.selectOne("BoardMapper.viewContent",input);
		
		// 조회수 업데이트
		int reandnumResult = sqlSession.update("BoardMapper.updateReadNum",input);
		if(result == null) {
			throw new NullPointerException("result == null");
		}
		if(reandnumResult == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}

	// 페이지 네이션 : 게시글 전체 수
	@Override
	public int totalContent() throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardMapper.totalContent");
		return result;
	}
	
	// 게시글 수정
	@Override
	public int updateContent(Board input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardMapper.updateContent",input);
		if(result == 0 ) {
			throw new NullPointerException("result ==  0");
		}
		return result;
	}
	
	// 비회원 게시글 수정 비밀번호 확인 
	@Override
	public Board nonMemberPwChk(Board input) throws Exception {
		Board result = null;
		result = sqlSession.selectOne("BoardMapper.nonMemberPwChk",input);
		return result;
	}

	// 게시물 삭제
	@Override
	public int deleteContent(Board input) throws Exception {
		int result=0;
		result = sqlSession.delete("BoardMapper.deleteContent",input);
		if(result == 0 ) {
			throw new NullPointerException("result ==  0");
		}
		return 0;
	}
	
	
	// 첨부 파일
	@Override
	public int attachFile(BoardFile input) throws Exception {
		int result=0;
		result = sqlSession.insert("BoardMapper.attachFile",input);
		if(result == 0 ) {
			throw new NullPointerException("result ==  0");
		}
		return result;
	}
	
    // 첨부 파일 조회
	@Override
	public List<BoardFile> attachFileList(int input) throws Exception {
		List<BoardFile> result=null;
		result = sqlSession.selectList("BoardMapper.attachFileList",input);
		return result;
	}

	// 첨부 파일 삭제
	@Override
	public int attachFileDelete(BoardFile input) throws Exception {
		int result=0;
		result = sqlSession.delete("BoardMapper.attachFileDelete",input);
		if(result == 0 ) {
			throw new NullPointerException("result ==  0");
		}
		return result;
	}
}
