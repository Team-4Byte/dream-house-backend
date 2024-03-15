package com.ssafy.holmes.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.GugunCodeDto;
import com.ssafy.holmes.model.SidoCodeDto;
import com.ssafy.holmes.model.mapper.AddressMapper;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressMapper sidoGugunMapper;

    @Autowired
    public AddressServiceImpl(AddressMapper sidoGugunMapper) {
        super();
        this.sidoGugunMapper = sidoGugunMapper;
    }

    @Override
    public List<GugunCodeDto> getGugunCodes(String sidoCode) throws SQLException {
        return sidoGugunMapper.getGugunCodes(sidoCode);
    }

    @Override
    public List<SidoCodeDto> getSidoCodes() throws SQLException {
        return sidoGugunMapper.getSidoCodes();
    }

    @Override
    public List<DongCodeDto> getDongCodes(String gugunCode) throws SQLException {
        return sidoGugunMapper.getDongCodes(gugunCode);
    }

    @Override
    public List<DongCodeDto> getHiddenGetDongCodes(String gugunName) throws SQLException {
        return sidoGugunMapper.getHiddenGetDongCodes(gugunName);
    }

    @Override
    public String getGugunName(String gugunCode) throws SQLException {
        return sidoGugunMapper.getGugunName(gugunCode);
    }
}