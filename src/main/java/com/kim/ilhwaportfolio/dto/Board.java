package com.kim.ilhwaportfolio.dto;

import oracle.sql.DATE;

public class Board {
	private int boardnum;
	private String subject;
	private String content;
	private String img;
	private int writer;
	private String writerid;
	private String pwd;
	private DATE regdate;
	private DATE edidate;
	private int readnum;
	private int hit;

	// 디비 조회 관련 변수
	private int start;
	private int end;
	private String keyword;
	private String search;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getBoardnum() {
		return boardnum;
	}

	public void setBoardnum(int boardnum) {
		this.boardnum = boardnum;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getWriter() {
		return writer;
	}

	public void setWriter(int writer) {
		this.writer = writer;
	}

	public String getWriterid() {
		return writerid;
	}

	public void setWriterid(String writerid) {
		this.writerid = writerid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public DATE getRegdate() {
		return regdate;
	}

	public void setRegdate(DATE regdate) {
		this.regdate = regdate;
	}

	public DATE getEdidate() {
		return edidate;
	}

	public void setEdidate(DATE edidate) {
		this.edidate = edidate;
	}

	public int getReadnum() {
		return readnum;
	}

	public void setReadnum(int readnum) {
		this.readnum = readnum;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Board [boardnum=" + boardnum + ", subject=" + subject + ", content=" + content + ", img=" + img
				+ ", writer=" + writer + ", writerid=" + writerid + ", pwd=" + pwd + ", regdate=" + regdate
				+ ", edidate=" + edidate + ", readnum=" + readnum + ", hit=" + hit + "start" + +start + "end" + end
				+ "]";
	}
}
