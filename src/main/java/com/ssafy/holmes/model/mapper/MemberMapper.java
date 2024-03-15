package com.ssafy.holmes.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.holmes.model.MemberDto;

@Mapper
public interface MemberMapper {

	MemberDto login(MemberDto memberDto) throws SQLException;

	MemberDto userInfo(String userId) throws SQLException;

	void saveRefreshToken(Map<String, String> map) throws SQLException;

	Object getRefreshToken(String userid) throws SQLException;

	void deleteRefreshToken(Map<String, String> map) throws SQLException;

	boolean register(MemberDto dto) throws SQLException;

	boolean delete(String userId) throws SQLException;

	boolean modify(MemberDto memberDto) throws SQLException;
}
