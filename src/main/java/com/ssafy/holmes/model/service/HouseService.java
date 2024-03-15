package com.ssafy.holmes.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.holmes.model.BookMarkDto;
import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.HouseDealDto;
import com.ssafy.holmes.model.HouseInfoDto;

public interface HouseService {

	HouseInfoDto getHouseInfo(int aptCode) throws SQLException;

	List<HouseDealDto> getHouseDealsByDongCode(String dongCode) throws SQLException;

	List<HouseDealDto> getHouseDealsByAptCode(int aptCode) throws SQLException;

	void pressAptFavor(BookMarkDto bookMarkDto) throws SQLException;

	void UnPressAptFavor(BookMarkDto bookMarkDto) throws SQLException;

	List<DongCodeDto> getDongCodes(String gugunCode) throws SQLException;

	List<HouseInfoDto> getAptList(String dongCode) throws SQLException;

	List<Integer> getFiveListAptCode() throws SQLException;

	HouseInfoDto getFiveListAptName(int aptCode) throws SQLException;

	int getTradeCount(int aptCode) throws SQLException;

	List<HouseInfoDto> getHouseInfoByaptName(String aptName) throws SQLException;

	boolean isPress(BookMarkDto bookMarkDto) throws SQLException;
}