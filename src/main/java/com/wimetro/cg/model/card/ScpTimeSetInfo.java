package com.wimetro.cg.model.card;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @title: ScpTimeSetInfo
 * @author: Ellie
 * @date: 2023/05/17 09:38
 * @description: 控制器和时间组
 **/
@Data
@ApiModel(value = "新增控制器时间组")
public class ScpTimeSetInfo {
    @ApiModelProperty(value = "控制器sn")
    private String sn;

    @ApiModelProperty(value = "新增时间组")
    private List<Integer> timeSetList;
}
