package com.wimetro.cg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: ScpInfo
 * @author: Ellie
 * @date: 2023/05/17 09:48
 * @description:
 **/
@Data
@ApiModel(value = "控制器参数")
public class ScpInfo {
    @ApiModelProperty(value = "控制器sn")
    private String sn;
}
