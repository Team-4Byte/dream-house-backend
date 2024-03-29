package com.ssafy.holmes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.holmes.model.MemberDto;
import com.ssafy.holmes.model.service.MemberService;
import com.ssafy.holmes.util.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Api(tags = "회원 정보", description = "Version 1.0")
@Slf4j
public class MemberController {

	private MemberService memberService;
	private JWTUtil jwtUtil;

	public MemberController(MemberService memberService, JWTUtil jwtUtil) {
		super();
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
	}

	// @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response =
	// Map.class)
	// @GetMapping("/info/{userId}")
	// public ResponseEntity<Map<String, Object>> getInfo(
	// @PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true)
	// String userId,
	// HttpServletRequest request) {
	// log.info("request : {} ", request);
	// Map<String, Object> resultMap = new HashMap<>();
	// HttpStatus status = HttpStatus.ACCEPTED;
	// if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
	// log.info("사용 가능한 토큰!!!");
	// try {
	// // 로그인 사용자 정보.
	// MemberDto memberDto = memberService.userInfo(userId);
	// resultMap.put("userInfo", memberDto);
	// status = HttpStatus.OK;
	// } catch (Exception e) {
	// log.error("정보조회 실패 : {}", e);
	// resultMap.put("message", e.getMessage());
	// status = HttpStatus.INTERNAL_SERVER_ERROR;
	// }
	// } else {
	// log.error("사용 불가능 토큰!!!");
	// status = HttpStatus.UNAUTHORIZED;
	// }
	// return new ResponseEntity<Map<String, Object>>(resultMap, status);
	// }

	@ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		// log.debug("login user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = memberService.login(memberDto);
			if (loginUser != null) {
				String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());

				// 발급받은 refresh token을 DB에 저장.
				memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);

				// JSON으로 token 전달.
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);

				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
				status = HttpStatus.UNAUTHORIZED;
			}

		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> removeToken(
			@PathVariable("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			memberService.deleRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원가입", notes = "회원을 등록한다.", response = Map.class)
	@PostMapping
	private ResponseEntity<?> register(@RequestBody MemberDto member) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			memberService.register(member);
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			log.error("회원가입 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(memberService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(memberDto.getUserId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원 삭제", notes = "회원을 삭제한다.", response = Map.class)
	@DeleteMapping("/{userid}")
	private ResponseEntity<?> delete(@PathVariable String userid) throws Exception {
		MemberDto mem = memberService.userInfo(userid);
		if (mem != null) {
			boolean res = memberService.delete(userid);
			if (res) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.internalServerError().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ApiOperation(value = "회원 수정", notes = "회원을 수정한다.", response = Map.class)
	@PutMapping
	private ResponseEntity<?> modify(@RequestBody MemberDto member)
			throws Exception {

		// User target = userService.getUser(userid);
		MemberDto mem = memberService.userInfo(member.getUserId());
		if (mem != null) {
			mem.setUserName(member.getUserName());
			mem.setUserPassword(member.getUserPassword());
			mem.setEmailId(member.getEmailId());
			mem.setEmailDomain(member.getEmailDomain());
			boolean res = memberService.modify(mem);

			if (res) {
				return ResponseEntity.ok(mem);
			} else {
				return ResponseEntity.internalServerError().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// @ApiOperation(value = "아파트 찜 목록", notes = "사용자가 아파트 찜한 목록을 받아온다.", response =
	// Map.class)
	// @GetMapping("/favorApt")
	// public ResponseEntity<?> getFavorAptList(String userId) {
	// List<BookMark> list = memberService.getFavorAptList(userId);
	// return ResponseEntity.ok().build();
	// }
}