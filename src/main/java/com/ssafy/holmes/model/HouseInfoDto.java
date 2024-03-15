package com.ssafy.holmes.model;

import lombok.Data;

@Data
public class HouseInfoDto {
    private int aptCode;
    private String aptName;
    private String dongCode;
    private String dongName;
    private int buildYear;
    private String jibun;
    private String lat;
    private String lng;
    private Double avgAmount;
    private int tradeAmount;
}
