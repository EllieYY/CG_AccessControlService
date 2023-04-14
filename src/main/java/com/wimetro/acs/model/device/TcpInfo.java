package com.wimetro.acs.model.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: TcpInfo
 * @author: Ellie
 * @date: 2023/04/10 09:18
 * @description:
 **/
@ApiModel(value = "tcp信息")
@Data
public class TcpInfo {
    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "子网掩码")
    private String mask;

    @ApiModelProperty(value = "网关")
    private String gateway;

    @ApiModelProperty(value = "mac地址")
    private String mac;

    @ApiModelProperty(value = "tcp通信端口")
    private int tcpPort;

    @ApiModelProperty(value = "通信密码")
    private String password;
}
