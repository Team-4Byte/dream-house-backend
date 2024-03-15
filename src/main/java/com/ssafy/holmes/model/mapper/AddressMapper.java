package com.ssafy.holmes.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.GugunCodeDto;
import com.ssafy.holmes.model.SidoCodeDto;

@Mapper
public interface AddressMapper {
    public List<GugunCodeDto> getGugunCodes(String sidoCode) throws SQLException;

    public List<SidoCodeDto> getSidoCodes() throws SQLException;

    public List<DongCodeDto> getDongCodes(String gugunCode) throws SQLException;

    public List<DongCodeDto> getHiddenGetDongCodes(String gugunName) throws SQLException;

    public String getGugunName(String gugunCode) throws SQLException;
}