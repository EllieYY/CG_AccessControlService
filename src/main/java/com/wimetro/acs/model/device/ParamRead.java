package com.wimetro.acs.model.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @title: ParamRead
 * @author: Ellie
 * @date: 2023/04/10 08:44
 * @description:
 **/
@Data
@ApiModel(value = "参数读取参数")
public class ParamRead {
    @ApiModelProperty(value = "设备标识码sn（16位字符）")
    private String sn;
}
