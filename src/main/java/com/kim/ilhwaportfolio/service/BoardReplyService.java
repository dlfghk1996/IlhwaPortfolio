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
	public int replyDelete(int input) throws Exception;
	// 부모 댓글 삭제
	public int parentReplyDelete(int input) throws Exception;
	// 비밀번호 확인
	public Board_reply replyPwCheck(Board_reply input) throws Exception;
	
	
	/**대댓글*/
	// 자식 순번 가져오기
	public Board_reply getChildrenSeq(int input) throws Exception;
	// MAX 순번 가져오기
	public int replyMaxSeq(int input) throws Exception;
	// 댓글 삭제후 댓글 순번 재정렬
	public int deleteReplySeqUpdate(int input) throws Exception;
    // 자식 조회
	public int getReplyChildren(int input) throws Exception;
	// 같은 순번일 경우 +1
	public int replySeqRearrange(Board_reply input) throws Exception;

}
