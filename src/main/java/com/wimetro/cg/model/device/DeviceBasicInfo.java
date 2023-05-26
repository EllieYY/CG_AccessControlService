package com.wimetro.cg.model.device;

import com.wimetro.cg.protocol.scp.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @title: DeviceBasicInfo
 * @author: Ellie
 * @date: 2023/04/10 09:16
 * @description:
 **/
@Data
@ApiModel(value = "设备基础信息")
public class DeviceBasicInfo {
    @ApiModelProperty(value = "设备sn")
    private String sn;

    @ApiModelProperty(value = "通信参数")
    private TcpInfo tcpInfo;

    @ApiModelProperty(value = "卡容量参数")
    private CardCapacity cardCapacity;

    @ApiModelProperty(value = "设备运行参数")
    private OperatingParam operatingParam;

    @ApiModelProperty(value = "功能参数")
    private FuncationParam funcationParam;

    @ApiModelProperty(value = "设备版本")
    private String version;

    @ApiModelProperty(value = "黑名单报警开关")
    private boolean blacklistAlramOn;

    @ApiModelProperty(value = "控制器防拆报警开关")
    private boolean tamperAlamOn;

    @ApiModelProperty(value = "记录存储方式")
    private int recordType;

    public void setDeviceVersion(DeviceVersion version) {
        if (!Objects.isNull(version)) {
            this.version = version.getVersion() + "." + version.getVersion2();
        }
    }

    public void setBL(BlacklistSW blacklistSW) {
        if (!Objects.isNull(blacklistSW)) {
            blacklistAlramOn = blacklistSW.getSw() > 0 ? true : false;
        }
    }

    public void setTamperSW(TamperAlarmSW tamperAlarmSW) {
        if (!Objects.isNull(tamperAlarmSW)) {
            tamperAlamOn = tamperAlarmSW.getSw() > 0 ? true : false;
        }
    }

    public void setRecordType(RecodeSaveType recodeSaveType) {
        if (!Objects.isNull(recodeSaveType)) {
            recordType = recodeSaveType.getType();
        }
    }

}
