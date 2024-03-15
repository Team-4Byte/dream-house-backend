package com.ssafy.holmes.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.GugunCodeDto;
import com.ssafy.holmes.model.SidoCodeDto;

public interface AddressService {
    public List<GugunCodeDto> getGugunCodes(String sidoCode) throws SQLException;

    public List<SidoCodeDto> getSidoCodes() throws SQLException;

    public List<DongCodeDto> getDongCodes(String gugunCode) throws SQLException;

    public List<DongCodeDto> getHiddenGetDongCodes(String gugunName) throws SQLException;

    public String getGugunName(String gugunCode) throws SQLException;
}