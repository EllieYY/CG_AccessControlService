package com.wimetro.cg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @title: ScpInfo
 * @author: Ellie
 * @date: 2023/05/17 09:48
 * @description:
 **/
@Data
@ApiModel(value = "控制器列表参数")
public class ScpListInfo {
    @ApiModelProperty(value = "控制器sn列表")
    private List<String> snList;
}
