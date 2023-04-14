package com.wimetro.acs.model.device;

import com.wimetro.acs.protocol.TcpParamOperation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @title: DeviceShadow
 * @author: Ellie
 * @date: 2023/03/27 09:54
 * @description:
 **/
@Data
@AllArgsConstructor
public class DeviceShadow {
    private String sn;         // 设备sn
    private String password;
    private TcpParamOperation tcpParam;

//    private String ip;      // 设备ip
//    private int port;       // 设备tcp端口
//    private String mac;     // mac地址
//    private String mask;    // 子网掩码
//    private String gateway; // 网关

    private boolean registered;    // 是否注册-添加到系统中
    private int netIdentifier;     // 搜索设备的网络标识

    public CGDeviceTcpInfo toDeviceWebInfo() {
        CGDeviceTcpInfo item = new CGDeviceTcpInfo();
        item.setIp(tcpParam.getIp());
        item.setPort(tcpParam.getTcpPort());
        item.setSn(sn);
        item.setDeviceType(sn.substring(0, 8));
        item.setDeviceSerial(sn.substring(8, 16));
        item.setMac(tcpParam.getMac());
        return item;
    }

    public void fromTcpParam(String sn, TcpParamOperation operation) {
        this.setSn(sn);

        // TODO:
    }
}
