package com.kim.ilhwaportfolio.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	
	@Autowired
	FileUtils fileUtils;

	/** 게시판 */
	// 데이터 형태를 JSON으로 보내주면, 서버쪽에서도 JSON으로 받아야 한다.
	@RequestMapping(value = "board", method = RequestMethod.GET)
	public String board(Model model, Board board,
			@RequestParam(value = "nowPage", defaultValue = "1", required = false) int pnum) throws Exception {
		/** 페이지 네이션 변수 설정 */
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
		model.addAttribute("board", board);
		model.addAttribute("contentList", contentList);
		model.addAttribute("pagenation", pagenation);
		return "board/board";
	}

	/** 글쓰기 페이지 */
	@RequestMapping(value = "boardwrite", method = RequestMethod.GET)
	public String boardwrite() {
		return "board/boardWrite";
	}

	/** 글쓰기 do */
	@RequestMapping(value = "postContent", method = RequestMethod.POST)
	public String postContent(Board board, List<MultipartFile> file) throws Exception {
		
		/** 세션에 저장 되어 있는 현재 로그인 되어있는 사용자의 memberkey를 가지고 온다. */
		Member member = (Member) webHelper.getSession("members", null);
		// 회원/비회원 일 구분
		if (member != null) {
			board.setWriter(member.getMembernum());
		}
		int result = boardService.postContent(board);
		if (result == 0) {
			System.out.println("insert 예외처리 실행");
		}
		// 첨부 파일이 있을 경우 BoardFileDTO에 저장
		FileUtils fileUtils = new FileUtils();
		List<BoardFile> filelist = fileUtils.saveBordDTO(file);
		for (BoardFile f : filelist) {
			f.setBoardnum(board.getBoardnum());
			int insertResult = boardService.attachFile(f);
			if (insertResult == 0) {
				System.out.println("예외처리실행");
			}
		}
		String url = "redirect:boardView?boardnum=" + board.getBoardnum();
		return url;
	}

	/** 상세 게시글 보기 */
	@RequestMapping(value = "boardView", method = RequestMethod.GET)
	public String viewContent(@RequestParam("boardnum") int boardnum, Model model) throws Exception {
		
		Board board = boardService.viewContent(boardnum);
		if (board == null) {
			System.out.println("예외처리 실행");
		}
		// 첨부파일
		List<BoardFile> attachFileList = boardService.attachFileList(boardnum);
		if (attachFileList != null) {
			model.addAttribute("attachFileList", attachFileList);
		}
		// 이전글 다음글
		Map<String, Object> prevNextMap = new HashMap<String, Object>(); // 권한처리 생략
		prevNextMap = boardService.prevNext(boardnum);

		model.addAttribute("prevNextMap", prevNextMap);
		model.addAttribute("board", board);
		return "board/boardView";
	}

	/** 글 수정 페이지 회원 */
	@RequestMapping(value = "boardUpdate", method = RequestMethod.POST)
	public String boardUpdate(Board board, Model model, HttpServletRequest request) throws Exception {

		// 회원 수정
		Member member = (Member) webHelper.getSession("members", "");
		// 회원의 수정 페이지  접근권한 확인
		if(member != null) {
			if (member.getMembernum() != board.getWriter()) {
				webHelper.redirect("접근권한이 없습니다.");
			}
		// 비회원 수정페이지 접근 권한 확인
		}else {
			board = boardService.nonMemberPwChk(board);
			if(board == null) {
				webHelper.redirect("접근권한이 없습니다.");
			}
		}
		
		// 첨부파일 목록 
		List<BoardFile> boardfile = boardService.attachFileList(board.getBoardnum());
		if (boardfile != null) {
			model.addAttribute("boardfile", boardfile);
		}
		model.addAttribute("board", board);
		return "board/boardUpdate";
	}

	/** 글 수정 do */
	@RequestMapping(value = "updateContent", method = RequestMethod.POST)
	public String updateContent(Board board, MultipartHttpServletRequest request) throws Exception {
		
		List<MultipartFile> fileList = request.getFiles("file");
		// 첨부파일 업데이트
		FileUtils fileUtils = new FileUtils();
		List<BoardFile> filelist = fileUtils.saveBordDTO(fileList);
		for (BoardFile f : filelist) {
			f.setBoardnum(board.getBoardnum());
			int insertResult = boardService.attachFile(f);
			if (insertResult == 0) {
				System.out.println("예외처리실행");
			}
		}
		int result = boardService.updateContent(board);
		if (result == 0) {
			System.out.println("예외처리 실행");
		}
		return "redirect:boardView?boardnum=" + board.getBoardnum();
	}

	/** 게시글 삭제 */
	@RequestMapping(value = "deleteContent", method = RequestMethod.POST)
	public String deleteContent(Board board) throws Exception {
		
		Member member = (Member) webHelper.getSession("members", null);
		
		// 세션에 로그인이 안되어있을 경우 -> 비회원 비밀번호 확인
		if(member == null) {
			board = boardService.nonMemberPwChk(board);
			if(board == null) {
				webHelper.redirect("접근권한이 없습니다.");
			}
		// 세션에 저장된 회원정보와 게시글 작성자 pk 비교
		}else {
			if (member.getMembernum() != board.getWriter()) {
				webHelper.redirect("접근권한이 없습니다.");
			}
		}
		/**게시글 삭제 전 첨부파일 및 본문 이미지 삭제*/
		// 첨부파일 삭제
		List<BoardFile> boardFile= boardService.attachFileList(board.getBoardnum());
		if(!boardFile.isEmpty()) {
			if(fileUtils.fileDelete(boardFile)) {
				boardService.attachFileDelete(boardFile.get(0));
			}else {
				System.out.println("파일 삭제 예외처리");
			}
		}
        // 본문 이미지 삭제
		fileUtils.findImgPath(board.getContent());
		boardService.deleteContent(board);
		
		return "redirect:board";
	} 

	/** 썸머 노트 이미지 업로드  */
	@ResponseBody
	@RequestMapping(value = "imgUpload", method = RequestMethod.POST)
	public String imgUpload(MultipartHttpServletRequest request) throws Exception {
		
		List<MultipartFile> fileList = request.getFiles("uploadfile");
		FileUtils fileUtils = new FileUtils();
		String filename = fileUtils.uploadFile(fileList);
		return filename;
	}

	/** 첨부파일 다운로드  */
	@ResponseBody
	@RequestMapping(value = "attachFileDownload", method = RequestMethod.GET)
	public void attachFileDownload(@RequestParam("filepath") String filepath, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");

		File f = new File("C:/ILHAW/boardFileUpload/" + filepath);
		FileUtils fileUtils = new FileUtils();
		Map<String, Object> map = new HashMap<>();
		map.put("downloadFile", f);
		fileUtils.renderMergedOutputModel(map, request, response);
	}

	/** 첨부파일 삭제 
	@ResponseBody
	@RequestMapping(value = "attachFileDelete", method = RequestMethod.POST)
	public int attachFileUpdate(BoardFile boardfile) throws Exception {
		FileUtils fileUtils = new FileUtils();
		int DeleteResult = 0;
		if (fileUtils.fileDelete(boardfile)) {
			DeleteResult = boardService.attachFileDelete(boardfile);
		}
		return DeleteResult;
	}*/

	/** 댓글 작성 */
	@RequestMapping(value = "replyWrite", method = RequestMethod.POST)
	@ResponseBody
	public String replyWrite(Board_reply board_reply) throws Exception {
		
		// 대댓글 인 경우 
		if(board_reply.getParent() != 0) {
			// 부모 depth+1,형제 댓글의 MAX(seq) 를 가져온다.  
			Board_reply childReply = boardReplyService.getChildrenSeq(board_reply.getParent());
			// 등록할려는 댓글객체의 필드에 삽입한다.
			board_reply.setDepth(childReply.getDepth());
			board_reply.setSeq(childReply.getSeq());
			// 기존 순번이 현재 순번과 같거나 클경우 +1 을 해준다.
			boardReplyService.replySeqRearrange(board_reply);
		}else {
		// 댓글인 경우
			int maxseq = boardReplyService.replyMaxSeq(board_reply.getReply_boardnum());
			board_reply.setSeq(maxseq);
		}
		int result = boardReplyService.addReply(board_reply);

		return "";
	}

	/** 댓글 목록 : */
	// json 데이터를 리턴
	@RequestMapping(value = "replyList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> replyList(@RequestParam int boardnum) throws Exception {
		
		List<Board_reply> replyList = boardReplyService.getReplyList(boardnum);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("replyList", replyList);
		return map;
	}

	/** 댓글 (삭제/ 수정) 시  비밀번호 확인 */
	@RequestMapping(value = "replyPwCheck", method = RequestMethod.POST)
	public @ResponseBody int replyPwCheck(@RequestBody Map<String, String> parm) throws Exception {
		
		Board_reply board_reply = new Board_reply();
		board_reply.setReplynum(Integer.parseInt(parm.get("replynum")));
		board_reply.setReply_password(parm.get("reply_password"));
		
		int replyPwCheckResult = 0;
		// 댓글의 비밀번호 확인
		Board_reply reply = boardReplyService.replyPwCheck(board_reply);
		if (reply != null) {
			// 삭제를 위한 비밀번호 확인 일 경우
			if (parm.get("value").equals("delete")) {
				// 삭제할려는 댓글의 자식댓글 유무를 확인한다.
				int numberofChildren = boardReplyService.getReplyChildren(reply.getReplynum());
				// 자식댓글이 있는 경우, 해당 댓글과 자식댓글까지 전부 삭제한다.
				if(numberofChildren > 0) {
					boardReplyService.parentReplyDelete(reply.getReplynum());
				// 자식댓글이 없는 경우, 해당 댓글만 삭제한다.
				}else {
					//boardReplyService.deleteReplySeqUpdate(reply.getReplynum());
					boardReplyService.replyDelete(reply.getReplynum());
				}
			}
			replyPwCheckResult = 1;
		}
		return replyPwCheckResult;
	}

	/** 댓글 수정 */
	@RequestMapping(value = "replyUpdate", method = RequestMethod.POST)
	public @ResponseBody int replyUpdate(Board_reply board_reply) throws Exception {
		
		int replyUpdateResult = boardReplyService.replyUpdate(board_reply);
		return replyUpdateResult;
	}

	/** 회원 댓글 삭제 */
	@RequestMapping(value = "memberReplyDelete", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody int memberReplyDelete(@RequestParam("replynum") int replynum) throws Exception {
		// 삭제할려는 댓글의 자식댓글 유무를 확인한다.
		int numberofChildren = boardReplyService.getReplyChildren(replynum);
		int result = 0;
		if(numberofChildren > 0) {
			// 자식댓글이 있는 경우, 해당 댓글과 자식댓글까지 전부 삭제한다.
			result = boardReplyService.parentReplyDelete(replynum);
		}else {
			// 자식댓글이 없는 경우, 해당 댓글만 삭제한다.
			//boardReplyService.deleteReplySeqUpdate(replynum);
			result = boardReplyService.replyDelete(replynum);
		}
		return result;
	}
}
