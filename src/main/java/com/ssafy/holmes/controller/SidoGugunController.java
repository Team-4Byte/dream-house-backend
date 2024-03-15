package com.ssafy.holmes.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.GugunCodeDto;
import com.ssafy.holmes.model.SidoCodeDto;
import com.ssafy.holmes.model.service.AddressService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/address")
@Api(tags = "시도/구군 리스트 정보", description = "Version 1.0")
@Slf4j
public class SidoGugunController {
    private AddressService addressService;

    public SidoGugunController(AddressService addressService) {
        super();
        this.addressService = addressService;
    }

    @ApiOperation(value = "구군 정보", notes = "시도에 속한 구군 리스트를 반환한다.", response = List.class)
    @GetMapping("/gugun/{sidoCode}")
    public ResponseEntity<List<GugunCodeDto>> doSearchGuguns(@PathVariable String sidoCode) throws SQLException {
        List<GugunCodeDto> gugunCodes = addressService.getGugunCodes(sidoCode);
        if (gugunCodes != null)
            return ResponseEntity.ok(gugunCodes);
        else
            return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "시도 정보", notes = "전국의 시도 리스트를 반환한다.", response = List.class)
    @GetMapping("/list")
    public ResponseEntity<List<SidoCodeDto>> doListInit() throws SQLException {
        List<SidoCodeDto> sidoCodes = addressService.getSidoCodes();
        if (sidoCodes != null)
            return ResponseEntity.ok(sidoCodes);
        else
            return ResponseEntity.internalServerError().build();
    }

    @ApiOperation(value = "동 정보", notes = "구군에 속한 동 리스트를 반환한다.", response = List.class)
    @GetMapping("/dong/{gugunCode}")
    public ResponseEntity<List<DongCodeDto>> doSearchDongs(@PathVariable String gugunCode) throws SQLException {
        List<DongCodeDto> dongCodes = addressService.getDongCodes(gugunCode);
        if (dongCodes != null) {
            if (dongCodes.size() == 0) {
                String gugunName = addressService.getGugunName(gugunCode);
                List<DongCodeDto> list = addressService.getHiddenGetDongCodes(gugunName);
                return ResponseEntity.ok(list);
            } else {
                return ResponseEntity.ok(dongCodes);
            }

        } else
            return ResponseEntity.notFound().build();

    }
}
