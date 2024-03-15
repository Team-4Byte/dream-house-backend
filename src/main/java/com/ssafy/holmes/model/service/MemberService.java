package com.ssafy.holmes.model.service;

import java.sql.SQLException;

import com.ssafy.holmes.model.MemberDto;

public interface MemberService {

	MemberDto login(MemberDto memberDto) throws Exception;

	MemberDto userInfo(String userId) throws Exception;

	void saveRefreshToken(String userId, String refreshToken) throws Exception;

	Object getRefreshToken(String userId) throws Exception;

	void deleRefreshToken(String userId) throws Exception;

	boolean register(MemberDto dto) throws SQLException;

	boolean delete(String userId) throws SQLException;

	boolean modify(MemberDto memberDto) throws SQLException;
}
