package com.kim.ilhwaportfolio.service;

import java.util.List;
import java.util.Map;

import com.kim.ilhwaportfolio.dto.Board;
import com.kim.ilhwaportfolio.dto.BoardFile;

public interface BoardService {

	// 게시물 등록
	public int postContent(Board input)throws Exception;
	
	// 게시물  출력 
	public List<Board> contentList(Board input) throws Exception;
	
	// 단일 게시물 전체 출력 
	public Board viewContent(int input) throws Exception;
	
	// 이전글 다음글
	public Map<String,Object> prevNext(int input) throws Exception;
	
	// 페이지 네이션 : 게시글 전체 수
	public int totalContent() throws Exception;
	
	// 글수정
	public int updateContent(Board input) throws Exception;
	
	// 비회원 글수정 비밀번호 확인
	public Board nonMemberPwChk(Board input) throws Exception;
	
	// 첨부파일
	public int attachFile(BoardFile input) throws Exception;
	
	// 첨부파일조회
	public List<BoardFile> attachFileList(int input) throws Exception;
	
	// 첨부파일 삭제
	public int attachFileDelete(BoardFile input) throws Exception;
	
	
}
