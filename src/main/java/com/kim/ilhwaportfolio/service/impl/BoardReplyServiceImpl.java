package com.kim.ilhwaportfolio.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kim.ilhwaportfolio.dto.Board_reply;
import com.kim.ilhwaportfolio.service.BoardReplyService;

@Service
public class BoardReplyServiceImpl implements BoardReplyService{
	@Autowired
	SqlSession sqlSession;
	
	// 댓글 등록
	@Override
	public int addReply(Board_reply input) throws Exception {
		int result = 0;
		result = sqlSession.insert("BoardReplyMapper.addReply",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}

	// 댓글 목록
	@Override
	public List<Board_reply> getReplyList(int input) throws Exception {
		List<Board_reply> result = null;
		result = sqlSession.selectList("BoardReplyMapper.getReplyList",input);
		return result;
	}

	// 댓글 수정
	@Override
	public int replyUpdate(Board_reply input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.replyUpdate",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}

	// 댓글 비밀번호 확인
	@Override
	public Board_reply replyPwCheck(Board_reply input) throws Exception {
		Board_reply result = null;
		result = sqlSession.selectOne("BoardReplyMapper.replyPwCheck",input);
		return result;
	}

	// 댓글 삭제
	@Override
	public int replyDelete(Board_reply input) throws Exception {
		int result = 0;
		result = sqlSession.delete("BoardReplyMapper.replyDelete",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}
	
    // 댓글 순번 업데이트
	@Override
	public int replySeqUpdate(Board_reply input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.replySeqUpdate",input);
		return result;
	}
	
	// 댓글 삭제후 재정렬
	@Override
	public int deleteReplySeqUpdate(Board_reply input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.deleteReplySeqUpdate",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}
	
	// 자식 조회
	@Override
	public int getReplyChildren(int input) throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardReplyMapper.getReplyChildren",input);
		return result;
	}

	// 부모 댓글 삭제
	@Override
	public int parentReplyDelete(int input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.parentReplyDelete",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}


}
