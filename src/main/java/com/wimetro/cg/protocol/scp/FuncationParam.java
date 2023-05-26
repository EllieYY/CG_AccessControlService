package com.wimetro.cg.protocol.scp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wimetro.cg.protocol.message.OperationResult;
import com.wimetro.cg.util.ProtocolFiledUtil.CmdProp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: FuncationParam
 * @author: Ellie
 * @date: 2023/04/10 09:31
 * @description:
 **/
@ApiModel(value = "功能参数")
@Data
public class FuncationParam extends OperationResult {
    @ApiModelProperty(value = "记录存储方式")
    @CmdProp(index = 0, len = 1, deCodec = "bytesToInt")
    private int recordSaveMode;

    @ApiModelProperty(value = "键盘模式")
    @CmdProp(index = 1, len = 1, deCodec = "bytesToInt")
    private int keyboardType;


    @ApiModelProperty(value = "占位", hidden = true)
    @JsonIgnore
    @CmdProp(index = 2, len = 4, deCodec = "bytesToHexStr")
    private String bk;

    @ApiModelProperty(value = "互锁参数", hidden = true)
    @JsonIgnore
    @CmdProp(index = 3, len = 4, deCodec = "bytesToHexStr")
    private String interlocking;

    @ApiModelProperty(value = "消防报警参数")
    @CmdProp(index = 4, len = 1, deCodec = "bytesToInt")
    private int fireAlarm;

    @ApiModelProperty(value = "匪警报警参数")
    @CmdProp(index = 5, len = 1, deCodec = "bytesToInt")
    private int banditAlarm;

    @ApiModelProperty(value = "读卡间隔时间，单位秒")
    @CmdProp(index = 6, len = 2, deCodec = "bytesToInt")
    private int readerInterval;

    @ApiModelProperty(value = "语音播报开关", hidden = true)
    @JsonIgnore
    @CmdProp(index = 7, len = 10, deCodec = "bytesToHexStr")
    private String voiceBroadcast;

    @ApiModelProperty(value = "读卡校验")
    @CmdProp(index = 8, len = 1, deCodec = "bytesToInt")
    private int dataCheckType;

    @ApiModelProperty(value = "主板蜂鸣器")
    @CmdProp(index = 9, len = 1, deCodec = "bytesToInt")
    private int buzzerFlag;

    @ApiModelProperty(value = "烟雾报警类型")
    @CmdProp(index = 10, len = 1, deCodec = "bytesToInt")
    private int smokeAlarmType;

    @ApiModelProperty(value = "人员数量限制", hidden = true)
    @JsonIgnore
    @CmdProp(index = 11, len = 20, deCodec = "bytesToHexStr")
    private String limit;

    @ApiModelProperty(value = "门内人数", hidden = true)
    @JsonIgnore
    @CmdProp(index = 12, len = 20, deCodec = "bytesToHexStr")
    private String amount;

    @ApiModelProperty(value = "防盗主机参数", hidden = true)
    @JsonIgnore
    @CmdProp(index = 13, len = 13, deCodec = "bytesToHexStr")
    private String param;

    @ApiModelProperty(value = "防回潜类型")
    @CmdProp(index = 14, len = 1, deCodec = "bytesToInt")
    private int apbType;

    @ApiModelProperty(value = "卡片到期提醒")
    @CmdProp(index = 15, len = 1, deCodec = "bytesToInt")
    private int cardExpireWarn;

    @ApiModelProperty(value = "定时播报参数", hidden = true)
    @JsonIgnore
    @CmdProp(index = 16, len = 10, deCodec = "bytesToHexStr")
    private String timerBoardcast;
}
