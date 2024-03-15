package com.ssafy.holmes.model;

import lombok.Data;

@Data
public class HouseDealDto {
    private int no;
    private int aptCode;
    private String dealAmount;
    private int dealYear;
    private int dealMonth;
    private int dealDay;
    private String area;
    private String floor;

    private HouseInfoDto dto;
}