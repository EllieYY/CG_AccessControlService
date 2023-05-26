package com.wimetro.cg.model.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @title: DeviceAddParam
 * @author: Ellie
 * @date: 2023/04/06 09:31
 * @description: 设备添加参数
 **/
@Data
@ApiModel(value = "CG设备基础信息")
public class DeviceAddParam {
    @ApiModelProperty(value = "设备ip，格式xxx.xxx.xxx.xxx")
    @Pattern(regexp = "([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.([1-9]?\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$")
    private String ip;

    @ApiModelProperty(value = "设备标识码sn（16位字符）")
    private String sn;

    @ApiModelProperty(value = "设备mac（12位字符）")
    private String mac;
}
