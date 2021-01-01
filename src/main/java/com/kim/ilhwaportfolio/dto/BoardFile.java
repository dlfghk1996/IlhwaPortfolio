package com.kim.ilhwaportfolio.dto;

public class BoardFile {
	
	private int filenum; 
	private int boardnum;        // 게시글 pk 참조
	private String originalfilename;  // 파일 origin이름
	private String filepath;  // 파일 경로
	private String filetype; // 파일 확장자
	
	public BoardFile() {
		// TODO Auto-generated constructor stub
	}
	
	public BoardFile(int filenum, int boardnum, String originalfilename, String filepath,
			String filetype) {
		super();
		this.filenum = filenum;
		this.boardnum = boardnum;
		this.originalfilename = originalfilename;
		this.filepath = filepath;
		this.filetype = filetype;
	}

	public int getFilenum() {
		return filenum;
	}
	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}
	public int getBoardnum() {
		return boardnum;
	}
	public void setBoardnum(int boardnum) {
		this.boardnum = boardnum;
	}
	public String getOriginalfilename() {
		return originalfilename;
	}
	public void setOriginalfilename(String originalfilename) {
		this.originalfilename = originalfilename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	@Override
	public String toString() {
		return "BoardFile [filenum=" + filenum + ", boardnum=" + boardnum + ", originalfilename=" + originalfilename
				+ ", filepath=" + filepath + ", filetype=" + filetype + "]";
		}
	}

