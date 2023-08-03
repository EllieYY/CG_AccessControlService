package com.wimetro.cg.model.device;

import com.wimetro.cg.protocol.port.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @title: PortInfo
 * @author: Ellie
 * @date: 2023/04/12 15:03
 * @description:
 **/
@Data
@ApiModel(value = "端口信息")
public class CGPortInfo {
    @ApiModelProperty(value = "端口号， 0-常闭 1-常开 2-异常")
    private int port;

    @ApiModelProperty(value = "门磁继电器状态， 0-常闭 1-常开 2-异常")
    private int relayState;

    @ApiModelProperty(value = "运行状态 0-常闭 1-常开")
    private int mode;

    @ApiModelProperty(value = "门磁开关 0-关 1-开")
    private int strikeState;

    @ApiModelProperty(value = "bit0-非法刷卡报警")
    private int invalidCardAlarm;
    @ApiModelProperty(value = "bit1-门磁报警")
    private int strikeAlarm;
    @ApiModelProperty(value = "bit2-胁迫报警")
    private int forcedOpenAlarm;
    @ApiModelProperty(value = "bit3-开门超时报警")
    private int heldOpenAlarm;
    @ApiModelProperty(value = "bit4-黑名单报警")
    private int blacklistAlarm;
    @ApiModelProperty(value = "bit5-读卡器防拆报警")
    private int tamperReaderAlarm;

    @ApiModelProperty(value = "门锁定状态")
    private int lockState;

    // 胁迫报警相关
    @ApiModelProperty(value = "胁迫报警是否启用")
    private int forcedAlaramOn;

    @ApiModelProperty(value = "胁迫报警模式")
    private int forcedAlaramMode;

    @ApiModelProperty(value = "胁迫报警密码")
    private String forcedAlarmPwd;

    // 读卡器信息
    @ApiModelProperty(value = "读卡器字节数")
    private int readerBytes;

    // 继电器信息
    @ApiModelProperty(value = "继电器输出方式")
    private int relayMode;

    @ApiModelProperty(value = "开锁输出时长")
    private int heldOnTime;

    @ApiModelProperty(value = "按5秒常开")
    private int fiveSecOnMode;


    public void setForcedInfo(ForcedInfo forcedInfo) {
        this.forcedAlaramOn = forcedInfo.getValid();
        this.forcedAlaramMode = forcedInfo.getMode();
        this.forcedAlarmPwd = forcedInfo.getPassword();
    }

    public void setHeldOnTime(HeldOnTime heldOnTime) {
        if (Objects.nonNull(heldOnTime)) {
            this.heldOnTime = heldOnTime.getSeconds();
        }
    }

    public void setHeldOnMode(HeldOnInfo heldOnInfo) {
        if (Objects.nonNull(heldOnInfo)) {
            // TODO:判断位根据测试情况核对
            this.heldOnTime = heldOnInfo.getMode5seconds();
        }
    }

}
