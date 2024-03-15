package com.ssafy.holmes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.holmes.model.BoardDto;
import com.ssafy.holmes.model.BoardLike;
import com.ssafy.holmes.model.BoardListDto;
import com.ssafy.holmes.model.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/board")
@Api(tags = "게시판 정보", description = "Version 1.0")
@Slf4j
public class BoardController {

	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@ApiOperation(value = "게시판 글목록", notes = "게시글의 정보를 반환한다.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping
	public ResponseEntity<?> listArticle(
			@RequestParam @ApiParam(value = "게시글을 얻기위한 부가정보.", required = true) Map<String, String> map) {
		log.info("listArticle map - {}", map);
		try {
			BoardListDto boardListDto = boardService.getBySearchArticle(map);
			HttpHeaders header = new HttpHeaders();
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "수정 할 글 얻기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
	@GetMapping("/modify/{articleno}")
	public ResponseEntity<BoardDto> getModifyArticle(
			@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}

	@ApiOperation(value = "게시판 글 수정", notes = "수정할 게시글 정보를 입력(성공=success, 실패=fail 반환)", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyArticle(
			@RequestBody @ApiParam(value = "수정할 글 정보.", required = true) BoardDto boardDto) throws Exception {
		boardService.modifyArticle(boardDto);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "게시판 글 작성", notes = "새로운 게시글 정보를 입력")
	@PostMapping
	public ResponseEntity<?> writeArticle(
			@RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto) {
		try {
			boardService.writeArticle(boardDto);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "게시판 글 내용 보기", notes = "글번호에 해당하는 게시글의 정보를 반환", response = BoardDto.class)
	@GetMapping("/{articleno}")
	public ResponseEntity<BoardDto> getArticle(
			@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		boardService.updateHit(articleno);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}

	@ApiOperation(value = "게시판 글 삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제(성공=success, 실패=fail 반환)", response = String.class)
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(
			@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("deleteArticle - 호출");
		boardService.deleteArticle(articleno);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "게시판 글 좋아요 개수", notes = "해당 개시글의 좋아요 개수를 반환", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found Page"),
			@ApiResponse(code = 500, message = "Server Error") })
	@GetMapping("/like/{articleno}")
	public ResponseEntity<Integer> getArticleLikeCount(
			@ApiParam(value = "좋아요 개수를 받아올 글 번호.", required = true) int articleNo) throws Exception {
		int count = boardService.getArticleLikeCount(articleNo);

		return ResponseEntity.ok(count);
	}

	@ApiOperation(value = "게시판 글 좋아요 누르기", notes = "해당 개시글을 좋아요 (추가 또는 삭제)", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found Page"),
			@ApiResponse(code = 500, message = "Server Error") })
	@PostMapping("/like/{articleno}")
	public ResponseEntity<String> pressArticleLike(
			@ApiParam(value = "좋아요를 누를 글 번호.", required = true) int articleNo) throws Exception {

		boardService.pressArticleLike(articleNo);

		return ResponseEntity.ok().build();
	}

	// 사용자 인증 필요
	@ApiOperation(value = "게시판 글 좋아요 확인하기", notes = "눌렀다면 1, 안 눌렀다면 0", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found Page"),
			@ApiResponse(code = 500, message = "Server Error") })
	@PostMapping("/like/{articleno}/like-status")
	public ResponseEntity<Integer> getLikeStatus(
			@ApiParam(value = "글 번호", required = true) @PathVariable("articleno") int articleNo,
			@ApiParam(value = "사용자 아이디", required = true) @RequestParam String userId) throws Exception {

		int check = boardService.isPressArticleLike(new BoardLike(articleNo, userId));

		return ResponseEntity.ok(check);
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}