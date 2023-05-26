package com.wimetro.cg.model.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: CGDeviceBasicInfo
 * @author: Ellie
 * @date: 2023/03/20 11:12
 * @description: 设备基础信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CG设备通信信息")
public class CGDeviceTcpInfo {
    @ApiModelProperty(value = "设备sn：前8位是产品类型，后8位是设备序号")
    private String sn;

    @ApiModelProperty(value = "mac")
    private String mac;

    @ApiModelProperty(value = "设备ip，格式为x.x.x.x")
    private String ip;

    @ApiModelProperty(value = "设备tcp通信端口")
    private int port;

    @ApiModelProperty(value = "产品类型，sn前8位字符")
    private String deviceType;

    @ApiModelProperty(value = "设备序号，sn后8位字符")
    private String deviceSerial;
}
