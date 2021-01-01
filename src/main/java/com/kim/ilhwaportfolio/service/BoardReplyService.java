package com.kim.ilhwaportfolio.service;

import java.util.List;

import com.kim.ilhwaportfolio.dto.Board_reply;

public interface BoardReplyService {

	// 댓글 등록
	public int addReply(Board_reply input) throws Exception;
	// 댓글 목록
	public List<Board_reply> getReplyList(int input) throws Exception;
	// 댓글 수정
	public int replyUpdate(Board_reply input) throws Exception;
	// 댓글 삭제
	public int replyDelete(Board_reply input) throws Exception;
	// 비밀번호 확인
	public Board_reply replyPwCheck(Board_reply input) throws Exception;
	
	/**대댓글*/
	// 댓글 max 순번 조회
	public int replyMaxSeq(int input) throws Exception;
	// 댓글  순번 업데이트
	public int replySeqUpdate(Board_reply input) throws Exception;
	// 대댓글 계층/순번 조회
	public Board_reply replyGetDepth(int input) throws Exception;
	// 댓글 삭제후 댓글 순번 재정렬
	public int deleteReplySeqUpdate(Board_reply input) throws Exception;
    // 자식 조회
	public int getReplyChildren(Board_reply input) throws Exception;

}