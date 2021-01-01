package com.kim.ilhwaportfolio.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.AbstractView;

import com.kim.ilhwaportfolio.dto.BoardFile;

@Component
public class FileUtils  extends AbstractView {
	private final String uploadPath ="C:\\ILHAW\\boardFileUpload\\";
	/** 1. 파일 업로드 처리 */

	/** 1.summernote */
	 public String uploadFile(List<MultipartFile> fileList)throws Exception{
		 String filename = "";
		 for (MultipartFile mf : fileList) {
			 filename = saveFileName(mf.getOriginalFilename(),"");
			 File dirPath = new File(this.uploadPath+filename); 
			 if (dirPath.exists()==false) { 
				 dirPath.mkdirs(); 
		     }
			 mf.transferTo(dirPath);
		 }
		 return "boardFileUpload/"+filename;
	 }
	 
	 
	 /** 2.첨부파일*/
	 public List<BoardFile> saveBordDTO(List<MultipartFile> fileList)throws Exception{
		 List<BoardFile> boardfileList = new ArrayList<BoardFile>();
		 String filename = "";
		 for (MultipartFile mf : fileList) {
			 if(!mf.isEmpty()) {
				 filename = saveFileName(mf.getOriginalFilename(),"attach");
				 String extension = StringUtils.getFilenameExtension(mf.getOriginalFilename()); // 파일 확장자
			     BoardFile boardfile = new BoardFile(0,0,mf.getOriginalFilename(),filename,extension);
			     boardfileList.add(boardfile);
				 File dirPath = new File(this.uploadPath+filename); 
				 if (dirPath.exists()==false) { 
					 dirPath.mkdirs(); 
			     }
				 mf.transferTo(dirPath);
			 } 
		 }
		 return boardfileList;
	 }
	 
	 
	 
	 //1-1 날짜별 폴더 생성 + 파일 이름 중복방지
	 private static String saveFileName(String originalFileName,String folderName) {
		 String uploadDate  = new SimpleDateFormat("yyMMdd").format(new Date());
		 String saveFileName = uploadDate +"/"+UUID.randomUUID().toString() + "_" + originalFileName;
		 saveFileName = folderName !=""?folderName+"/"+saveFileName:saveFileName;
		 return saveFileName;
	  }

	 /**3. 파일 다운로드 */
	 @Override	
	 public void renderMergedOutputModel(Map<String, Object> model,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		setContentType("application/download; charset=utf-8");
		File file = (File) model.get("downloadFile");
		
		response.setContentType(getContentType());
		response.setContentLength((int) file.length());

		String userAgent = request.getHeader("User-Agent");
		boolean ie = userAgent.indexOf("MSIE") > -1;
		String fileName = null;
		if (ie) {
			fileName = URLEncoder.encode(file.getName(), "utf-8");
		} else {
			fileName = new String(file.getName().getBytes("utf-8"),
					"iso-8859-1");
		}
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException ex) {
				}
		}
		out.flush();
	}

	/** 파일 삭제 처리 */
	// 파일 삭제처리 메서드 
	 public boolean fileDelete(BoardFile boardfile)throws Exception { 
		 File file = new File("C:/ILHAW/boardFileUpload/"+boardfile.getFilepath());
			 if( file.exists()) {
				 return file.delete();
		 	}
		  return false;
		 }
	}

