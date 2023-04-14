package com.wimetro.acs.protocol.scp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @title: OperatingParam
 * @author: Ellie
 * @date: 2023/04/10 09:22
 * @description:
 **/
@ApiModel(value = "设备运行参数")
@Data
public class OperatingParam extends OperationResult {
    @ApiModelProperty(value = "设备运行天数")
    @CmdProp(index = 0, len = 2, deCodec = "bytesToInt")
    private int onlineDays;

    @ApiModelProperty(value = "格式化次数")
    @CmdProp(index = 1, len = 2, deCodec = "bytesToInt")
    private int formattingTimes;

    @ApiModelProperty(value = "看门狗复位次数")
    @CmdProp(index = 2, len = 2, deCodec = "bytesToInt")
    private int watchdogResetTimes;

    @ApiModelProperty(value = "UPS供电状态")
    @CmdProp(index = 3, len = 1, deCodec = "bytesToInt")
    private int powerSupplyType;

    @ApiModelProperty(value = "系统温度")
    @CmdProp(index = 4, len = 2, deCodec = "bytesToTemper")
    private int temperature;

    @ApiModelProperty(value = "上电时间")
    @CmdProp(index = 5, len = 7, deCodec = "bytesToDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date powerOnTime;

    @ApiModelProperty(value = "DV12电压")
    @CmdProp(index = 6, len = 2, deCodec = "bytesToDV12V")
    private float dv12Voltage;
}
