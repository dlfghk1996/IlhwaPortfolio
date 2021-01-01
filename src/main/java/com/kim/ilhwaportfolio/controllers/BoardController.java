	package com.kim.ilhwaportfolio.controllers;
	
	import java.io.File;
	import java.io.IOException;
	import java.io.InputStream;
	import java.text.DateFormat;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.HashMap;
	import java.util.Iterator;
	import java.util.List;
	import java.util.Map;
	import java.util.UUID;
	
	import javax.activation.CommandMap;
	import javax.annotation.Resource;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;
	import org.springframework.web.multipart.MultipartFile;
	import org.springframework.web.multipart.MultipartHttpServletRequest;
	import org.springframework.web.servlet.ModelAndView;
	import org.springframework.web.servlet.mvc.support.RedirectAttributes;
	import org.springframework.web.servlet.support.RequestContextUtils;
	
	import com.fasterxml.jackson.core.JsonParser;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.kim.ilhwaportfolio.dto.Board;
	import com.kim.ilhwaportfolio.dto.BoardFile;
	import com.kim.ilhwaportfolio.dto.Board_reply;
	import com.kim.ilhwaportfolio.dto.Member;
	import com.kim.ilhwaportfolio.helper.FileUtils;
	import com.kim.ilhwaportfolio.helper.WebHelper;
	import com.kim.ilhwaportfolio.service.BoardReplyService;
	import com.kim.ilhwaportfolio.service.BoardService;
	
	@Controller
	public class BoardController {
	
			@Autowired
			BoardService boardService;
			
			@Autowired
			BoardReplyService boardReplyService;
			
			@Autowired
			WebHelper webHelper;
			
	/** 게시판 */
	// 데이터 형태를 JSON으로 보내주면, 서버쪽에서도 JSON으로 받아야 한다.
	@RequestMapping(value = "board", method = RequestMethod.GET)
	public String board(Model model,Board board,@RequestParam(value="nowPage",defaultValue = "1",required=false)int pnum)throws Exception{
		/** 페이지 네이션 변수 설정*/
		// 현재 페이지
		int nowPage = pnum;
		int totalCount = 0;
		// 한 페이지당 표시할 게시글 수
		int listCount = 10;
		// 한 그룹당 표시할 페이지 번호 수
		int pageCount = 4;
		totalCount = boardService.totalContent();
		String pagenation = webHelper.pagenation("board", nowPage, totalCount, listCount, pageCount);
		
		// 디비 조회 관련 setter
		board.setStart(webHelper.getStart());
		board.setEnd(webHelper.getEnd());
		
		List<Board> contentList = boardService.contentList(board);
		model.addAttribute("board",board);
		model.addAttribute("contentList",contentList);
		model.addAttribute("pagenation",pagenation);
		return "board/board";
	}
	
	
	/** 글쓰기 페이지  */
	@RequestMapping(value = "boardwrite", method = RequestMethod.GET)
	public String boardwrite() {
		return "board/boardWrite";
	}
	
	/** 글쓰기 do  */
	@RequestMapping(value = "postContent", method = RequestMethod.POST)
	// @RequestParam(required=false) MultipartHttpServletRequest file
	//
	public String postContent(Board board,List<MultipartFile> file )throws Exception{
		/**  세션에 저장 되어 있는 현재 로그인 되어있는 사용자의 memberkey를 가지고 온다.*/
		Member member = (Member)webHelper.getSession("members","dto");
		// 회원/비회원 일 구분
		if(member != null) {
			board.setWriter(member.getMembernum());
		}
		int result = boardService.postContent(board);
		if(result == 0) {
			System.out.println("insert 예외처리 실행");
		}
		// 첨부 파일이 있을 경우   BoardFileDTO에 저장
		FileUtils fileUtils = new FileUtils();
		List<BoardFile> filelist = fileUtils.saveBordDTO(file);
		for (BoardFile f : filelist) {
			f.setBoardnum(board.getBoardnum());
			int insertResult = boardService.attachFile(f);
			if(insertResult==0) {System.out.println("예외처리실행");}
		}
		String url = "redirect:boardView?boardnum="+ board.getBoardnum();
		return url;
	}
	
	/** 상세 게시글 보기  */
	@RequestMapping(value = "boardView", method = RequestMethod.GET)
	public String viewContent(@RequestParam("boardnum") int boardnum,Model model)throws Exception{
		System.out.println(boardnum);
		Board board = boardService.viewContent(boardnum);
		if(board == null) {
			System.out.println("예외처리 실행");
		}
		// 첨부파일
		List<BoardFile>attachFileList = boardService.attachFileList(boardnum);
		if(attachFileList!=null) {
			model.addAttribute("attachFileList",attachFileList);
		}
		// 이전글 다음글
		Map<String,Object> prevNextMap = new HashMap<String,Object>(); //권한처리 생략
		prevNextMap = boardService.prevNext(boardnum);
	
		model.addAttribute("prevNextMap",prevNextMap);
		model.addAttribute("board",board);
		return "board/boardView";
	}
	
	/** 글 수정 페이지  회원*/
	@RequestMapping(value = "boardUpdate",method = RequestMethod.GET)
	public String boardUpdate(@RequestParam(value="boardnum", required=false, defaultValue="0") int boardnum,Model model,HttpServletRequest request)throws Exception{
		Member member = (Member)webHelper.getSession("members", "");
	
		Map<String,? > flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			int nonMemberBoardNum = (int) flashMap.get("boardnums");
			boardnum = nonMemberBoardNum;
		}
		Board board = boardService.viewContent(boardnum);
		if(board == null) {
			webHelper.redirect("잘못된 접근 입니다. 존재하지않는 게시물 입니다.");
		}
		// 비회원이 다른 회원 게시물 접근 
		if(board.getPwd()!=null || member == null) { 
			if(flashMap == null ) {
				webHelper.redirect("접근권한이 없습니다.");
			}
		}else {
			// 회원이 다른 회원 게시물 접근
			if(member.getMembernum() != board.getWriter()) {webHelper.redirect("접근권한이 없습니다.");}
		}
		// 첨부파일
		List<BoardFile> boardfile = boardService.attachFileList(boardnum);
		if(boardfile != null) {
			model.addAttribute("boardfile",boardfile);
		}
		model.addAttribute("board",board);
		return "board/boardUpdate";
	}
	
	/** 비회원 수정 페이지 비밀 번호 확인  */
	@RequestMapping(value = "nonMemberPwChk", method = RequestMethod.POST)
	public String boardUpdate(Board board,RedirectAttributes redirect)throws Exception{
		board = boardService.nonMemberPwChk(board);
		if(board == null) {
			webHelper.redirect("접근권한이 없습니다.");
		}
		redirect.addFlashAttribute("boardnums", board.getBoardnum()); 
	    return "redirect:boardUpdate";
	
	}
	
	/** 글 수정  do */
	@RequestMapping(value = "updateContent", method = RequestMethod.POST)
	public String updateContent(Board board,MultipartHttpServletRequest request)throws Exception{
		List<MultipartFile> fileList = request.getFiles("file");
		//첨부파일 업데이트
		FileUtils fileUtils = new FileUtils();
		List<BoardFile> filelist = fileUtils.saveBordDTO(fileList);
		for (BoardFile f : filelist) {
			System.out.println("첨부파일 업데이트 for문 진입");
			f.setBoardnum(board.getBoardnum());
			int insertResult = boardService.attachFile(f);
			if(insertResult==0) {System.out.println("예외처리실행");}
		}
		int result= boardService.updateContent(board);
		if(result == 0) {
			System.out.println("예외처리 실행");
		}
		return "redirect:board";
	}
	
	/** 썸머 노트 이미지 업로드 */
	@ResponseBody
	@RequestMapping(value = "imgUpload", method = RequestMethod.POST)
	public String imgUpload(MultipartHttpServletRequest request)throws Exception{
		List<MultipartFile> fileList = request.getFiles("uploadfile");
		FileUtils fileUtils = new FileUtils();
		String filename = fileUtils.uploadFile(fileList);
		return filename;
	}
	
	/** 첨부파일 다운로드 */
	@ResponseBody
	@RequestMapping(value = "attachFileDownload", method = RequestMethod.GET)
	public void attachFileDownload(@RequestParam("filepath")String filepath,
			                       HttpServletRequest request, 
			                       HttpServletResponse response)throws Exception{
		request.setCharacterEncoding("utf-8");
	
		File f = new File("C:/ILHAW/boardFileUpload/"+filepath);
		FileUtils fileUtils = new FileUtils();
		Map<String, Object> map = new HashMap<>();
		map.put("downloadFile", f);
		fileUtils.renderMergedOutputModel(map,request,response);
	}
	
	
	/** 첨부파일 삭제 */
	@ResponseBody
	@RequestMapping(value = "attachFileDelete", method = RequestMethod.POST)
	public int attachFileUpdate(BoardFile boardfile)throws Exception{
		FileUtils fileUtils = new FileUtils();
		int DeleteResult=0;
		if(fileUtils.fileDelete(boardfile)) {
			DeleteResult = boardService.attachFileDelete(boardfile);
		}
		return DeleteResult;
	}
	
	/** 댓글 작성*/
	@RequestMapping(value = "replyWrite", method = RequestMethod.POST)
	@ResponseBody
	public String replyWrite(Board_reply board_reply)throws Exception{
		System.out.println(board_reply.toString());
		if(board_reply.getParent()!=0) {
			// 대댓글 
			// 부모 댓글의 마지막 계층 구하기 
			Board_reply replyInfo = boardReplyService.replyGetDepth(board_reply.getParent());
			board_reply.setDepth(replyInfo.getDepth());
			board_reply.setSeq(replyInfo.getSeq());
			//**순서 업데이트 관련 메서드 **//
			 //기존 순번과 새로운 순번이 같거나 새로운 순번이 더 크다면 기존순번에 +1을 해준다.
			boardReplyService.replySeqUpdate(board_reply);
		}else {
			// 댓글순번
			int replyMaxSeq = boardReplyService.replyMaxSeq(board_reply.getReply_boardnum());
			System.out.println(replyMaxSeq);
			board_reply.setSeq(replyMaxSeq);
		}
        // 댓글 등록
		int result = boardReplyService.addReply(board_reply);
		return "";
	}
	
	/** 댓글 목록 : */
	// json 데이터를 리턴
	@RequestMapping(value = "replyList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> replyList(@RequestParam int boardnum)throws Exception{
		List<Board_reply> replyList = boardReplyService.getReplyList(boardnum);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("replyList", replyList);
		System.out.println("map" + map);
		return map;
	}
	
	/** 댓글  ( 삭제/ 수정 )  비밀번호 확인*/
	// json 데이터를 리턴
	@RequestMapping(value = "replyPwCheck", method = RequestMethod.POST)
	@ResponseBody
	public int replyPwCheck(@RequestBody Map<String,String> parm)throws Exception{
		Board_reply board_reply = new Board_reply();
		board_reply.setReplynum(Integer.parseInt(parm.get("replynum")));
		board_reply.setReply_password(parm.get("reply_password"));
		int replyPwCheckResult = 0;
		// 수정, 삭제 비밀번호 확인
		Board_reply reply = boardReplyService.replyPwCheck(board_reply);
		if(reply != null) {
			if(parm.get("value").equals("delete")) {
				int childrenResult = boardReplyService.getReplyChildren(reply);
				if(childrenResult <= 0) { 
					replyPwCheckResult = boardReplyService.replyDelete(reply)>0?2:0;
					//삭제하려는 대상의 댓글보다 순번이 큰 댓글들의 순서(reorder) 값은 1씩 빼서 순서 값을 맞춰 준다.
				   // boardReplyService.deleteReplySeqUpdate(reply);
				}	
			}else {
				replyPwCheckResult = 1;
			}
		}
		return replyPwCheckResult;
	}
	
	/** 댓글 수정*/
	@RequestMapping(value = "replyUpdate", method = RequestMethod.POST)
	@ResponseBody
	public int replyUpdate(@RequestBody Board_reply board_reply)throws Exception{
		
	int replyUpdateResult = boardReplyService.replyUpdate(board_reply);
	return replyUpdateResult;
	}
	
	/** 댓글 삭제 */
	@RequestMapping(value = "replyDelete", method = RequestMethod.POST)
	@ResponseBody
	public int replyDelete(@RequestBody Board_reply board_reply)throws Exception{
		
	int result = 0;
	// session member 
	Member member = (Member)webHelper.getSession("members","");
	board_reply.setReply_writer(member.getMembernum());
	
	int childrenResult = boardReplyService.getReplyChildren(board_reply);
	if(childrenResult <= 0) { 
		result = boardReplyService.replyDelete(board_reply);
		// 삭제하려는 대상의 댓글보다 순번이 큰 댓글들의 순서(reorder) 값은 1씩 빼서 순서 값을 맞춰 준다.
	    //boardReplyService.deleteReplySeqUpdate(reply);
	}	
	return result;
	}

}
	
