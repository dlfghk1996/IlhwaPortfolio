package com.kim.ilhwaportfolio.dto;

import oracle.sql.DATE;

public class Board_reply {

	private int replynum;                       // pk
	private int reply_boardnum;                // 게시글 번호 (FK) : 같은 주제를 갖는 게시물의 고유번호. 부모글과 부모글로부터 파생된 모든 자식글은 같은 번호를 갖는다.
	private int parent;                   // 대댓글 부모 (null 값을 받기위해 wrapper class로 지정)
	private int seq;                     // 같은 그룹내 게시물의 순서
	private int depth;						// 대댓글 계층 :  같은 그룹내 계층
	private String reply;                  // 댓글 내용
	private int reply_writer;         // 회원 아이디 (FK) (null 값을 받기위해 wrapper class로 지정)
	private String reply_writer_nickname;// 비회원 닉네임
	private String reply_password;      // 비회원 비밀번호 
	private DATE regdate;              // 등록일
    private String del;

	public int getReplynum() {
		return replynum;
	}
	public void setReplynum(int replynum) {
		this.replynum = replynum;
	}
	public int getReply_boardnum() {
		return reply_boardnum;
	}
	public void setReply_boardnum(int reply_boardnum) {
		this.reply_boardnum = reply_boardnum;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public int getReply_writer() {
		return reply_writer;
	}
	public void setReply_writer(int reply_writer) {
		this.reply_writer = reply_writer;
	}
	public String getReply_writer_nickname() {
		return reply_writer_nickname;
	}
	public void setReply_writer_nickname(String reply_writer_nickname) {
		this.reply_writer_nickname = reply_writer_nickname;
	}
	public String getReply_password() {
		return reply_password;
	}
	public void setReply_password(String reply_password) {
		this.reply_password = reply_password;
	}
	public DATE getRegdate() {
		return regdate;
	}
	public void setRegdate(DATE regdate) {
		this.regdate = regdate;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getDel() {
		return del;
	}
	public void setDel(String del) {
		this.del = del;
	}
	@Override
	public String toString() {
		return "Board_reply [replynum=" + replynum + ", reply_boardnum=" + reply_boardnum + ", parent=" + parent + ", seq= "+ seq 
				+ ", depth=" + depth + ", reply=" + reply + ", reply_writer=" + reply_writer
				+ ", reply_writer_nickname=" + reply_writer_nickname + ", reply_password=" + reply_password
				+ ", regdate=" + regdate + "]";
		}
	}

