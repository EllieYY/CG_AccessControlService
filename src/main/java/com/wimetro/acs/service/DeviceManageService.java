package com.wimetro.acs.service;

import com.wimetro.acs.common.Constants;
import com.wimetro.acs.config.NettyConfig;
import com.wimetro.acs.model.CGPortInfo;
import com.wimetro.acs.model.device.CGDeviceTcpInfo;
import com.wimetro.acs.model.device.DeviceBasicInfo;
import com.wimetro.acs.model.device.DeviceShadow;
import com.wimetro.acs.netty.runner.ChannelManager;
import com.wimetro.acs.netty.runner.NettyTcpServer;
import com.wimetro.acs.netty.runner.NettyUdpServer;
import com.wimetro.acs.netty.runner.RequestPendingCenter;
import com.wimetro.acs.protocol.*;
import com.wimetro.acs.protocol.message.*;
import com.wimetro.acs.protocol.port.ForcedInfo;
import com.wimetro.acs.protocol.port.PortInfo;
import com.wimetro.acs.protocol.port.ReaderBytesInfo;
import com.wimetro.acs.protocol.port.RelayOutMode;
import com.wimetro.acs.protocol.scp.*;
import com.wimetro.acs.util.IdUtil;
import com.wimetro.acs.util.NetUtil;
import com.wimetro.acs.util.ToolConvert;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @title: DeviceManageService
 * @author: Ellie
 * @date: 2023/03/20 13:45
 * @description:
 **/
@Slf4j
@Service
public class DeviceManageService {
    private final NettyUdpServer udpServer;
    private final NettyTcpServer tcpServer;
    private final IdUtil idUtil;
    private final NettyConfig nettyConfig;
    public DeviceManageService(NettyUdpServer udpServer, NettyTcpServer tcpServer, IdUtil idUtil, NettyConfig nettyConfig) {
        this.udpServer = udpServer;
        this.tcpServer = tcpServer;
        this.idUtil = idUtil;
        this.nettyConfig = nettyConfig;
    }


    /**
     * 设备搜索：
     * udp 广播发送网络标识，网络标识不同的设备会进行回复
     * 设备回复Tcp信息后，设置搜索的网络标识，在后续重复搜索中，已回复的设备不再重复回复
     * 广播搜索发送多次
     * @return
     */
    public List<CGDeviceTcpInfo> deviceSearch() {
        // 构建广播报文
        MessageHeader header = new MessageHeader();
        header.setMsgCode(0x01FE00);
        String sn = "";
        for (int i = 0; i < 16; i++) {
            sn += "0";
        }
        header.setDeviceSN(sn);
        String pwd = "";
        for (int j = 0; j < 8; j++) {
            pwd += "f";
        }
        header.setDevicePwd(pwd);

        int streamId = IdUtil.randomRangeInt(0, 100000);
        header.setStreamId(streamId);
        NetIdentifierOperation body = new NetIdentifierOperation(idUtil.newNetIdentifier());

        // UDP发送广播报文
        ServerMessage serverMessage = new ServerMessage(header, body);
        InetSocketAddress remoteAddress = new InetSocketAddress("255.255.255.255", nettyConfig.getUdpServerPort());
        serverMessage.setTargetAddress(remoteAddress);

        try {
            for (int i = 0; i < 2; i++) {
                log.info("[broadcast] [{}] - {}", i, serverMessage);
                udpServer.udpSend(serverMessage);
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取设备搜索结果
        List<DeviceShadow> deviceShadowList = DeviceCenter.getUnRegistedDevice(idUtil.getNetIdentifier());

        List<CGDeviceTcpInfo> result = deviceShadowList.stream().collect(ArrayList::new, (list, item) -> {
            list.add(item.toDeviceWebInfo());
        }, ArrayList::addAll);

        return result;
    }

    /**
     * 设备添加
     * @param sn
     * @param ip
     * @param mac
     * @return
     */
    public DeviceBasicInfo deviceAdd(String sn, String ip, String mac) {
        DeviceBasicInfo deviceBasicInfo = new DeviceBasicInfo();
        deviceBasicInfo.setSn(sn);

        // 查询设备属性信息
        NoBodyOperation body = new NoBodyOperation();
        CardCapacity cardCapacity = (CardCapacity) getDeviceInfo(sn, body, OperationType.CARD_CAPACITY);
//        log.info("获取结果{}", cardCapacity);
        OperatingParam operatingParam = (OperatingParam) getDeviceInfo(sn, body, OperationType.OPERATING_PARAM);
//        log.info("获取结果{}", operatingParam);
        FuncationParam funcationParam = (FuncationParam) getDeviceInfo(sn, body, OperationType.FUNCTION_PARAM);
//        log.info("{}", funcationParam);
        TcpParamOperationResult tcpParam = (TcpParamOperationResult) getDeviceInfo(sn, body, OperationType.TCP_PARAM);
//        log.info("{}", tcpParam);
        // 版本号
        DeviceVersion deviceVersion = (DeviceVersion) getDeviceInfo(sn, body, OperationType.DEVICE_VERSION);
        log.info("{}", deviceVersion);
        // 黑名单报警开关
        BlacklistSW blacklistSW = (BlacklistSW) getDeviceInfo(sn, body, OperationType.BL_SWITCH);
        // 控制器防拆报警
        TamperAlarmSW tamperAlarmSW = (TamperAlarmSW) getDeviceInfo(sn, body, OperationType.TAMPER_ALARM_SWITCH);
        // 记录存储方式
        RecodeSaveType recodeSaveType = (RecodeSaveType) getDeviceInfo(sn, body, OperationType.RECORD_TYPE);


        if (!Objects.isNull(tcpParam)) {
            DeviceShadow deviceShadow = DeviceCenter.getDevice(sn);
            String pwd = deviceShadow.getPassword();
            deviceBasicInfo.setTcpInfo(tcpParam.toTcpInfo(pwd));
        }
        deviceBasicInfo.setCardCapacity(cardCapacity);
        deviceBasicInfo.setFuncationParam(funcationParam);
        deviceBasicInfo.setOperatingParam(operatingParam);
        deviceBasicInfo.setDeviceVersion(deviceVersion);
        deviceBasicInfo.setBL(blacklistSW);
        deviceBasicInfo.setTamperSW(tamperAlarmSW);
        deviceBasicInfo.setRecordType(recodeSaveType);

        return deviceBasicInfo;
    }

    public List<CGPortInfo> getPortInfo(String sn) {
        NoBodyOperation body = new NoBodyOperation();
        PortInfo portInfo = (PortInfo) getDeviceInfo(sn, body, OperationType.PORT_INFO);
        ReaderBytesInfo readerBytesInfo = (ReaderBytesInfo) getDeviceInfo(sn, body, OperationType.READER_BYTES);
        RelayOutMode relayOutMode = (RelayOutMode) getDeviceInfo(sn, body, OperationType.RELAY_OUT_INFO);

        log.info("{}", portInfo);
        log.info("{}", readerBytesInfo);
        log.info("{}", relayOutMode);

        if (Objects.isNull(portInfo)) {
            return new ArrayList<>();
        }
        List<CGPortInfo> portInfoList = portInfo.toCGPortInfo(readerBytesInfo, relayOutMode);

        log.info("{}", portInfoList);

        // 需要端口号
        for (CGPortInfo cgPort:portInfoList) {
            int port = cgPort.getPort();
            PortOperation portOperation = new PortOperation();
            portOperation.setPort(port);
            ForcedInfo forcedInfo = (ForcedInfo) getDeviceInfo(sn, portOperation, OperationType.FORCED_PWD_INFO);

            log.info("{}", forcedInfo);

            cgPort.setForcedInfo(forcedInfo);
        }

        return portInfoList;
    }


    /**
     * 设备连接
     * - 获取设备信息，设置设备的tcp模式为客户端，服务端信息为本服务tcp服务器信息（UDP通信）
     * -
     * @param ip
     */
    public boolean tcpParamSetting(String ip, String mac, String sn) {
        MessageHeader header = new MessageHeader();
        header.setMsgCode(0x010601);
        header.setDeviceSN(sn);
        String pwd = "ffffffff";
        header.setDevicePwd(pwd);
        int streamId = IdUtil.randomRangeInt(0, 100000);
        header.setStreamId(streamId);

        // 参数组装
        TcpParamOperation body = new TcpParamOperation();
        body.setMac(mac);
        body.setIp(ip);
        body.setMask("255.255.255.0");
        body.setGateway(ToolConvert.ip2gateway(ip));
        body.setDns("0.0.0.0");
        body.setAltDns("0.0.0.0");
        body.setTcpMode(1);   // tcp client 模式
        body.setTcpPort(nettyConfig.getTcpDevicePort());
        body.setUdpPort(nettyConfig.getUdpDevicePort());
        // TODO:部署到虚拟机上的，暂时屏蔽，因为服务器ip不对
        body.setTargetIp("192.168.2.194");   // 服务器ip
//        body.setTargetIp(NetUtil.getLocalHostIp());   // 本机ip

        body.setTargetPort(nettyConfig.getTcpServerPort());  //
        body.setAutoIp(70);
        body.setDomain("0");

        ServerMessage serverMessage = new ServerMessage(header, body);
        InetSocketAddress remoteAddress = new InetSocketAddress(ip, nettyConfig.getUdpDevicePort());
        serverMessage.setTargetAddress(remoteAddress);

        Future<DeviceMessage> future = RequestPendingCenter.add(streamId);

        log.info("[设置设备Tcp参数]  {}", serverMessage);
        udpServer.udpSend(serverMessage);

        try {
            // 获取结果，超时时间3秒
            DeviceMessage response = future.get(3, TimeUnit.SECONDS);
            if (response.getMessageHeader().getMsgCode() == Constants.CODE_OK) {
                // 添加设备管理-设备注册成功
                log.info("Tcp参数配置成功：{}", serverMessage);
                DeviceCenter.registerDevice(sn, pwd, body);
                return true;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }

        log.error("Tcp参数配置失败：{}", serverMessage);
        return false;
    }

    /**
     * 获取设备信息
     * @return
     */
    public OperationResult getDeviceInfo(String sn, Operation body, OperationType operationType) {
        int msgCode = operationType.getReadCode();
        // n - 查找设备ip | password | port
        DeviceShadow deviceShadow = DeviceCenter.getDevice(sn);
        if (Objects.isNull(deviceShadow)) {
            log.info("设备 {} 不存在", sn);
            return null;
        }

        TcpParamOperation tcpParam = deviceShadow.getTcpParam();
        String ip = tcpParam.getIp();
        String pwd = deviceShadow.getPassword();
        int deviceTcpPort = tcpParam.getTcpPort();

        // 报文头组装
        MessageHeader header = new MessageHeader();
        header.setMsgCode(msgCode);
        header.setDeviceSN(sn);
        header.setDevicePwd(pwd);
        int streamId = IdUtil.randomRangeInt(0, 100000);
        header.setStreamId(streamId);

        // 报文组装
        ServerMessage serverMessage = new ServerMessage(header, body);

        // 消息发送和等待
        Future<DeviceMessage> future = RequestPendingCenter.add(streamId);

        // 查找设备channel
        String key = ip + Constants.IP_PORT_SPLITTER + nettyConfig.getTcpServerPort();
        Channel channel = ChannelManager.getChannel(key);
        if (Objects.isNull(channel)) {
            log.info("设备连接通道已断开 {} - {}", ip, sn);
            return null;
        }
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        serverMessage.setTargetAddress(remoteAddress);
        tcpServer.tcpSend(serverMessage, channel);

        try {
            // 获取结果，超时时间3秒
            DeviceMessage response = future.get(10, TimeUnit.SECONDS);
            return response.getMessageBody();
        } catch (Exception e) {
            log.error("{}", e);
        }

        return null;
    }



    // 可发送Udp查询
    public TcpParamOperationResult getTcpParam(String sn) {
        MessageHeader header = new MessageHeader();
        header.setMsgCode(Constants.CODE_TCP_PARAM_READ);
        header.setDeviceSN(sn);
        header.setDevicePwd("ffffffff");
        int streamId = IdUtil.randomRangeInt(0, 100000);
        header.setStreamId(streamId);

        NoBodyOperation body = new NoBodyOperation();
        ServerMessage serverMessage = new ServerMessage(header, body);
        InetSocketAddress remoteAddress = new InetSocketAddress("192.168.2.27", nettyConfig.getUdpDevicePort());
        serverMessage.setTargetAddress(remoteAddress);

        Future<DeviceMessage> future = RequestPendingCenter.add(streamId);
        log.info("[查询Tcp参数] {} - {}", sn, serverMessage);
        udpServer.udpSend(serverMessage);

        try {
            // 获取结果，超时时间3秒
            DeviceMessage response = future.get(2, TimeUnit.SECONDS);
//            log.info("{}", response);
            return (TcpParamOperationResult) response.getMessageBody();
        } catch (Exception e) {
            log.error("{}", e);
        }

        return null;
    }

}
