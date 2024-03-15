package com.ssafy.holmes.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GugunCodeDto : 구군정보", description = "구군의 이름을 나타낸다.")
public class GugunCodeDto {
    @ApiModelProperty(value = "구군코드")
    private String gugunCode;
    @ApiModelProperty(value = "구군이름")
    private String gugunName;
}