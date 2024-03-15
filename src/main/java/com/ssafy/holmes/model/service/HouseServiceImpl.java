package com.ssafy.holmes.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.holmes.model.BookMarkDto;
import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.HouseDealDto;
import com.ssafy.holmes.model.HouseInfoDto;
import com.ssafy.holmes.model.mapper.HouseMapper;

@Service
public class HouseServiceImpl implements HouseService {

	private HouseMapper mapMapper;

	public HouseServiceImpl(HouseMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	@Override
	public HouseInfoDto getHouseInfo(int aptCode) throws SQLException {
		return mapMapper.getHouseInfo(aptCode);
	}

	@Override
	public List<HouseDealDto> getHouseDealsByDongCode(String dongCode) throws SQLException {
		return mapMapper.getHouseDealsByDongCode(dongCode);
	}

	@Override
	public List<HouseDealDto> getHouseDealsByAptCode(int aptCode) throws SQLException {
		return mapMapper.getHouseDealsByAptCode(aptCode);
	}

	@Override
	public void pressAptFavor(BookMarkDto bookMarkDto) throws SQLException {
		mapMapper.pressAptFavor(bookMarkDto);
	}

	@Override
	public void UnPressAptFavor(BookMarkDto bookMarkDto) throws SQLException {
		mapMapper.UnPressAptFavor(bookMarkDto);
	}

	@Override
	public List<DongCodeDto> getDongCodes(String gugunCode) throws SQLException {
		return mapMapper.getDongCodes(gugunCode);
	}

	@Override
	public List<HouseInfoDto> getAptList(String dongCode) throws SQLException {
		return mapMapper.getAptList(dongCode);
	}

	@Override
	public List<Integer> getFiveListAptCode() throws SQLException {
		return mapMapper.getFiveListAptCode();
	}

	@Override
	public HouseInfoDto getFiveListAptName(int aptCode) throws SQLException {
		return mapMapper.getFiveListAptName(aptCode);
	}

	@Override
	public int getTradeCount(int aptCode) throws SQLException {
		return mapMapper.getTradeCount(aptCode);
	}

	@Override
	public List<HouseInfoDto> getHouseInfoByaptName(String aptName) throws SQLException {
		return mapMapper.getHouseInfoByaptName(aptName);
	}

	@Override
	public boolean isPress(BookMarkDto bookMarkDto) throws SQLException {
		return mapMapper.isPress(bookMarkDto);
	}
}