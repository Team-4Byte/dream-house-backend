package com.ssafy.holmes.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "DongCodeDto : 동정보", description = "동을 나타낸다.")
public class DongCodeDto {

    @ApiModelProperty(value = "동코드")
    private String dongCode;

    @ApiModelProperty(value = "시도이름")
    private String sidoName;

    @ApiModelProperty(value = "구군이름")
    private String gugunName;

    @ApiModelProperty(value = "동이름")
    private String dongName;
}