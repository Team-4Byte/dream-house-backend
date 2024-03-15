package com.ssafy.holmes.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.holmes.model.BookMarkDto;
import com.ssafy.holmes.model.DongCodeDto;
import com.ssafy.holmes.model.HouseDealDto;
import com.ssafy.holmes.model.HouseInfoDto;
import com.ssafy.holmes.model.service.HouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/house")
@Api(tags = "집 정보", description = "Version 1.0")
@Slf4j
public class HouseController {

	private HouseService houseService;

	public HouseController(HouseService houseService) {
		super();
		this.houseService = houseService;
	}

	@ApiOperation(value = "거래량 기준 최상위 3위까지", notes = "거래량을 기준으로 최상위 3위까지 아파트 이름을 반환한다.", response = List.class)
	@GetMapping("/list/five")
	public ResponseEntity<?> getFiveAptList()
			throws SQLException {
		List<Integer> list1 = houseService.getFiveListAptCode();
		List<HouseInfoDto> list2 = new ArrayList<>();

		for (int i = 0; i < list1.size(); i++) {
			HouseInfoDto dto = houseService.getFiveListAptName(list1.get(i));
			list2.add(dto);
		}

		List<HouseInfoDto> resultList = new ArrayList<>();

		for (int j = 0; j < list2.size(); j++) {
			HouseInfoDto infoDto = list2.get(j);

			List<HouseDealDto> dealDto = houseService.getHouseDealsByAptCode(infoDto.getAptCode());
			Double avgAmount = 0.0;
			int sum = 0;

			for (int k = 0; k < dealDto.size(); k++) {
				String str = dealDto.get(k).getDealAmount();
				String strWithoutComma = str.replaceAll(",", "");
				sum += Integer.parseInt(strWithoutComma);
			}

			avgAmount = (double) (sum / dealDto.size());
			infoDto.setAvgAmount(avgAmount);
			infoDto.setTradeAmount(houseService.getTradeCount(infoDto.getAptCode()));
			resultList.add(infoDto);
		}

		if (resultList != null)
			return ResponseEntity.ok(resultList);
		else
			return ResponseEntity.internalServerError().build();
	}

	@ApiOperation(value = "집 정보", notes = "집에 대한 정보를 반환한다.", response = List.class)
	@GetMapping("/{aptCode}")
	public ResponseEntity<?> doHouseDetail(@ApiParam(value = "아파트코드", required = true) int aptCode)
			throws SQLException {
		HouseInfoDto list = houseService.getHouseInfo(aptCode);

		List<HouseDealDto> dealDto = houseService.getHouseDealsByAptCode(list.getAptCode());
		Double avgAmount = 0.0;
		int sum = 0;

		for (int k = 0; k < dealDto.size(); k++) {
			String str = dealDto.get(k).getDealAmount();
			String strWithoutComma = str.replaceAll(",", "");
			sum += Integer.parseInt(strWithoutComma);
		}
		avgAmount = (double) (sum / dealDto.size());
		list.setAvgAmount(avgAmount);

		if (list != null)
			return ResponseEntity.ok(list);
		else
			return ResponseEntity.internalServerError().build();
	}

	@ApiOperation(value = "아파트 이름", notes = "아파트 이름에 해당하는 정보를 반환한다.", response = List.class)
	@GetMapping("/name/{aptName}")
	public ResponseEntity<List<HouseInfoDto>> getHouseInfoByaptName(
			@PathVariable String aptName)
			throws SQLException {
		List<HouseInfoDto> list = houseService.getHouseInfoByaptName(aptName);

		List<HouseInfoDto> result = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			HouseInfoDto dto = list.get(i);

			List<HouseDealDto> dealDto = houseService.getHouseDealsByAptCode(dto.getAptCode());
			Double avgAmount = 0.0;
			int sum = 0;

			for (int k = 0; k < dealDto.size(); k++) {
				String str = dealDto.get(k).getDealAmount();
				String strWithoutComma = str.replaceAll(",", "");
				sum += Integer.parseInt(strWithoutComma);
			}
			if (dealDto.size() == 0)
				continue;

			avgAmount = (double) (sum / dealDto.size());
			dto.setAvgAmount(avgAmount);
			result.add(dto);
		}

		if (result != null)
			return ResponseEntity.ok(result);
		else
			return ResponseEntity.internalServerError().build();
	}

	@ApiOperation(value = "(시/구군) 아파트 리스트", notes = "구군코드 기준으로 아파트 리스트를 반환한다.", response = List.class)
	@GetMapping("/list/gugun/{gugunCode}")
	public ResponseEntity<List<HouseInfoDto>> getAptListByGugunCode(@PathVariable String gugunCode)
			throws SQLException {
		List<DongCodeDto> dongCodes = houseService.getDongCodes(gugunCode);
		if (dongCodes == null)
			return ResponseEntity.notFound().build();

		List<HouseInfoDto> houseDeals = new ArrayList<>();
		for (int i = 0; i < dongCodes.size(); i++) {
			List<HouseInfoDto> list = houseService.getAptList(dongCodes.get(i).getDongCode());

			for (int j = 0; j < list.size(); j++) {
				HouseInfoDto infoDto = list.get(j);

				List<HouseDealDto> dealDto = houseService.getHouseDealsByAptCode(infoDto.getAptCode());
				Double avgAmount = 0.0;
				int sum = 0;

				if (dealDto.size() == 0)
					continue;

				for (int k = 0; k < dealDto.size(); k++) {
					String str = dealDto.get(k).getDealAmount();
					String strWithoutComma = str.replaceAll(",", "");
					sum += Integer.parseInt(strWithoutComma);
				}
				avgAmount = (double) (sum / dealDto.size());
				infoDto.setAvgAmount(avgAmount);
				houseDeals.add(infoDto);
			}
		}

		return ResponseEntity.ok(houseDeals);
	}

	@ApiOperation(value = "(시/구군/동) 아파트 리스트", notes = "구군코드 기준으로 아파트 리스트를 반환한다.", response = List.class)
	@GetMapping("/list/dong/{dongCode}")
	public ResponseEntity<List<HouseInfoDto>> getAptListByDongCode(@PathVariable String dongCode) throws SQLException {
		List<HouseInfoDto> list = houseService.getAptList(dongCode);
		List<HouseInfoDto> resultList = new ArrayList<>();

		for (int j = 0; j < list.size(); j++) {
			HouseInfoDto infoDto = list.get(j);

			List<HouseDealDto> dealDto = houseService.getHouseDealsByAptCode(infoDto.getAptCode());
			Double avgAmount = 0.0;
			int sum = 0;

			for (int k = 0; k < dealDto.size(); k++) {
				String str = dealDto.get(k).getDealAmount();
				String strWithoutComma = str.replaceAll(",", "");
				sum += Integer.parseInt(strWithoutComma);
			}
			if (dealDto.size() == 0)
				continue;
			avgAmount = (double) (sum / dealDto.size());
			infoDto.setAvgAmount(avgAmount);
			resultList.add(infoDto);
		}

		return ResponseEntity.ok(resultList);
	}

	@ApiOperation(value = "(시/구군) 아파트 거래내역", notes = "구군코드 기준으로 거래내역을 반환한다.", response = List.class)
	@GetMapping("/deal/gugun/{gugunCode}")
	public ResponseEntity<List<HouseDealDto>> getHousesByGugunCode(@PathVariable String gugunCode) throws SQLException {
		List<DongCodeDto> dongCodes = houseService.getDongCodes(gugunCode);
		if (dongCodes == null)
			return ResponseEntity.notFound().build();

		List<HouseDealDto> houseDeals = new ArrayList<>();

		for (int i = 0; i < dongCodes.size(); i++) {
			List<HouseDealDto> list = (houseService.getHouseDealsByDongCode(dongCodes.get(i).getDongCode()));

			for (int j = 0; j < list.size(); j++) {
				houseDeals.add(list.get(j));
			}
		}

		if (houseDeals != null) {
			return ResponseEntity.ok(houseDeals);
		}
		return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "(시/구군/동) 아파트 거래내역", notes = "동코드 기준으로 거래내역을 반환한다.", response = List.class)
	@GetMapping("/deal/dong/{dongCode}")
	public ResponseEntity<List<HouseDealDto>> getHousesByDongCode(@PathVariable String dongCode) throws SQLException {
		List<HouseDealDto> houseDeals = houseService.getHouseDealsByDongCode(dongCode);
		if (houseDeals != null) {
			return ResponseEntity.ok(houseDeals);
		}
		return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "(아파트코드) 거래내역", notes = "아파트코드 기준으로 거래내역을 반환한다.", response = List.class)
	@GetMapping("/deal/apt/{aptCode}")
	public ResponseEntity<List<HouseDealDto>> getHousesByname(@PathVariable int aptCode) throws SQLException {
		List<HouseDealDto> houseDeals = houseService.getHouseDealsByAptCode(aptCode);
		if (houseDeals != null) {
			return ResponseEntity.ok(houseDeals);
		}
		return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "아파트 찜하기", notes = "아파트 코드를 기준으로 찜한다.", response = List.class)
	@PostMapping()
	public ResponseEntity<?> pressAptFavor(@RequestBody BookMarkDto bookMarkDto) throws SQLException {
		houseService.pressAptFavor(bookMarkDto);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "아파트 찜 확인하기", notes = "아파트 코드와 유저 ID 기준으로 반환한다.", response = List.class)
	@GetMapping()
	public ResponseEntity<?> isPress(@RequestBody BookMarkDto bookMarkDto) throws SQLException {
		boolean isCheck = houseService.isPress(bookMarkDto);
		return ResponseEntity.ok(isCheck);
	}

	@ApiOperation(value = "아파트 찜 삭제하기", notes = "찜한 아파트를 삭제한다.", response = List.class)
	@DeleteMapping()
	public ResponseEntity<?> unPressAptFavor(@RequestBody BookMarkDto bookMarkDto) throws SQLException {
		houseService.UnPressAptFavor(bookMarkDto);
		return ResponseEntity.ok().build();
	}
}