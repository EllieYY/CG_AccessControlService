package com.wimetro.cg.service;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.db.service.impl.CgcgControllerServiceImpl;
import com.wimetro.cg.db.service.impl.CgcgPortServiceImpl;
import com.wimetro.cg.db.service.impl.DCgcgEmployeeDoorServiceImpl;
import com.wimetro.cg.db.service.impl.DSchedulesGroupDetailServiceImpl;
import com.wimetro.cg.model.device.*;
import com.wimetro.cg.model.card.ScheduleGroupInfo;
import com.wimetro.cg.model.card.ScpTimeSetAddParam;
import com.wimetro.cg.model.card.ScpTimeSetInfo;
import com.wimetro.cg.model.response.DeviceResopnseType;
import com.wimetro.cg.netty.runner.NettyTcpServer;
import com.wimetro.cg.netty.runner.NettyUdpServer;
import com.wimetro.cg.netty.runner.RequestPendingCenter;
import com.wimetro.cg.protocol.*;
import com.wimetro.cg.protocol.card.TimeSlotOfDay;
import com.wimetro.cg.protocol.message.*;
import com.wimetro.cg.protocol.port.*;
import com.wimetro.cg.protocol.scp.*;
import com.wimetro.cg.util.IdUtil;
import com.wimetro.cg.util.NetUtil;
import com.wimetro.cg.util.ToolConvert;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
    private final DSchedulesGroupDetailServiceImpl schedulesGroupDetailService;
    private final DCgcgEmployeeDoorServiceImpl employeeDoorService;
    private final CgcgControllerServiceImpl cgcgControllerService;
    private final CgcgPortServiceImpl portService;
    public DeviceManageService(NettyUdpServer udpServer, NettyTcpServer tcpServer, IdUtil idUtil, NettyConfig nettyConfig,
                               DSchedulesGroupDetailServiceImpl schedulesGroupDetailService,
                               DCgcgEmployeeDoorServiceImpl employeeDoorService,
                               CgcgControllerServiceImpl cgcgControllerService,
                               CgcgPortServiceImpl portService) {
        this.udpServer = udpServer;
        this.tcpServer = tcpServer;
        this.idUtil = idUtil;
        this.nettyConfig = nettyConfig;
        this.schedulesGroupDetailService = schedulesGroupDetailService;
        this.employeeDoorService = employeeDoorService;
        this.cgcgControllerService = cgcgControllerService;
        this.portService = portService;
    }

    /**
     * 程序启动后，对数据库中设备进行自动认证
     */
    @PostConstruct
    public void deivceRegisted() {
        // 查询设备数据
        List<DeviceShadow> deviceShadowList = cgcgControllerService.getValidDevice();
        log.info("[设备认证] - {}", deviceShadowList);

        // 组装认证
        for (DeviceShadow device:deviceShadowList) {
            DeviceCenter.registerDevice(device, nettyConfig.getTcpDevicePort());
        }
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
        CardCapacity cardCapacity = (CardCapacity) tcpServer.readDeviceInfo(sn, body, OperationType.CARD_CAPACITY);
//        log.info("获取结果{}", cardCapacity);
        OperatingParam operatingParam = (OperatingParam) tcpServer.readDeviceInfo(sn, body, OperationType.OPERATING_PARAM);
//        log.info("获取结果{}", operatingParam);
        FuncationParam funcationParam = (FuncationParam) tcpServer.readDeviceInfo(sn, body, OperationType.FUNCTION_PARAM);
//        log.info("{}", funcationParam);
        TcpParamOperationResult tcpParam = (TcpParamOperationResult) tcpServer.readDeviceInfo(sn, body, OperationType.TCP_PARAM);
//        log.info("{}", tcpParam);
        // 版本号
        DeviceVersion deviceVersion = (DeviceVersion) tcpServer.readDeviceInfo(sn, body, OperationType.DEVICE_VERSION);
        log.info("{}", deviceVersion);
        // 黑名单报警开关
        BlacklistSW blacklistSW = (BlacklistSW) tcpServer.readDeviceInfo(sn, body, OperationType.BL_SWITCH);
        // 控制器防拆报警
        TamperAlarmSW tamperAlarmSW = (TamperAlarmSW) tcpServer.readDeviceInfo(sn, body, OperationType.TAMPER_ALARM_SWITCH);
        // 记录存储方式
        RecodeSaveType recodeSaveType = (RecodeSaveType) tcpServer.readDeviceInfo(sn, body, OperationType.RECORD_TYPE);

        // 数据组装
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

    /**
     * 获取设备卡容量信息
     * @param sn
     * @return
     */
    public CardCapacity getDeviceCardCapacity(String sn) {
        NoBodyOperation body = new NoBodyOperation();
        CardCapacity cardCapacity = (CardCapacity) tcpServer.readDeviceInfo(sn, body, OperationType.CARD_CAPACITY);
        return cardCapacity;
    }

    /**
     * 获取端口信息
     * @param sn
     * @return
     */
    public List<CGPortInfo> getPortInfo(String sn) {
        NoBodyOperation body = new NoBodyOperation();
        PortInfo portInfo = (PortInfo) tcpServer.readDeviceInfo(sn, body, OperationType.PORT_INFO);
        ReaderBytesInfo readerBytesInfo = (ReaderBytesInfo) tcpServer.readDeviceInfo(sn, body, OperationType.READER_BYTES);
        RelayOutMode relayOutMode = (RelayOutMode) tcpServer.readDeviceInfo(sn, body, OperationType.RELAY_OUT_INFO);

        log.info("{}", portInfo);
        log.info("{}", readerBytesInfo);
        log.info("{}", relayOutMode);

        if (Objects.isNull(portInfo)) {
            return new ArrayList<>();
        }

        // 判断门数量
        String deviceType = sn.substring(0, 8);
        int doorCount = cgcgControllerService.getDoorCountByDeviceType(deviceType);
        List<CGPortInfo> portInfoList = portInfo.toCGPortInfo(doorCount, readerBytesInfo, relayOutMode);

        log.info("{}", portInfoList);

        // 需要端口号
        for (CGPortInfo cgPort:portInfoList) {
            int port = cgPort.getPort();
            PortOperation portOperation = new PortOperation();
            portOperation.setPort(port);
            ForcedInfo forcedInfo = (ForcedInfo) tcpServer.readDeviceInfo(sn, portOperation, OperationType.FORCED_PWD_INFO);
            HeldOnTime heldOnTime = (HeldOnTime) tcpServer.readDeviceInfo(sn, portOperation, OperationType.HELD_ON_TIME);
            HeldOnInfo heldOnInfo = (HeldOnInfo) tcpServer.readDeviceInfo(sn, portOperation, OperationType.HELD_ON_INFO);

            log.info("{}", forcedInfo);

            cgPort.setForcedInfo(forcedInfo);
            cgPort.setHeldOnTime(heldOnTime);
            cgPort.setHeldOnMode(heldOnInfo);
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
//        body.setTargetIp("192.168.2.194");   // 服务器ip
        body.setTargetIp(NetUtil.getLocalHostIp());   // 本机ip

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
            DeviceMessage response = future.get(10, TimeUnit.SECONDS);
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


    /** 时间组增加
     * - 查找对应关系
     * - 增加时间组
     */
    public void timesetAdd(ScpTimeSetAddParam scpTimeSetAddParam) {
        //
        List<ScpTimeSetInfo> scpTimeSetList = scpTimeSetAddParam.getList();
        for (ScpTimeSetInfo info:scpTimeSetList) {
            if (Objects.isNull(info)) {
                log.info("无时间组需要配置：{}");
                continue;
            }
            sendScpTimeSet(info);
        }
    }

    /**
     * 单个控制器时间组发送处理
     * @param info
     */
    private void sendScpTimeSet(ScpTimeSetInfo info) {

        String sn = info.getSn();
        List<Integer> schduleGroupList = info.getTimeSetList();

        // 获取数据
        List<ScheduleGroupInfo> groupInfoList = schedulesGroupDetailService.getScheduleGroupInfo(schduleGroupList);
        log.info("[时间组配置] {}", groupInfoList);

        // 组包发送
        List<TimeSetOperation> operationList = new ArrayList<>();
        for (ScheduleGroupInfo groupInfo:groupInfoList) {
            int groupId = groupInfo.getGroupId();
            List<TimeSlotOfDay> timeSlotOfDays = groupInfo.toDeviceInfo();

//            log.info("{} - {}", groupId, timeSlotOfDays);

            TimeSetOperation operation = new TimeSetOperation(groupId, timeSlotOfDays);
            operationList.add(operation);

            DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, OperationType.TIME_SET.getSettingCode());
            log.info("[时间组配置] - {}:{} - {}:{}", retCode.getCode(), retCode.getMsg(), groupId, timeSlotOfDays);
        }
//        log.info("operation: {}", operationList);
    }


    /** 时间组删除
     *  - 清除所有时间组
     *  - 查找对应关系
     *  - 增加时间组
     */
    public void timesetDelete(List<String> scpList) {
        Operation operation = new NoBodyOperation();
        for (String sn:scpList) {
            // 清除所有开门时段
            DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, Constants.CODE_TIMESET_CLEAR);
            log.info("[时间组删除——清除所有开门时间段] - {}:{}-{}", sn, retCode.getCode(), retCode.getMsg());
            if ((retCode != DeviceResopnseType.SUCCESS)) {
                continue;
            }

            // 查找控制器开门时段信息
            ScpTimeSetInfo scpTimeSetInfo = employeeDoorService.getScpTimeSetList(sn);

            // 添加控制器开门时段
            sendScpTimeSet(scpTimeSetInfo);
        }
    }


    /**
     * 设备初始化
     * @param sn
     */
    public DeviceResopnseType deviceInit(String sn) {
        Operation operation = new NoBodyOperation();

        DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, Constants.CODE_DEVICE_INIT);
        log.info("[设备初始化] - {}:{}-{}", sn, retCode.getCode(), retCode.getMsg());

        // 监控开启
        DeviceResopnseType retCode1 = tcpServer.deviceSetting(sn, operation, Constants.CODE_MONITOR_ON);
        log.info("[实时监控开启] - {}:{}", retCode1.getCode(), retCode1.getMsg());

        return retCode;
    }


    /**
     * 远程开关门
     * @param param
     * * @param openDoor: true-开门 false-关门
     * @return
     */
    public DeviceResopnseType doorOperation(DoorOperationParam param, boolean openDoor) {
        String sn = param.getSn();
        if (param.getDoorList().size() == 0) {
            return DeviceResopnseType.INVALID_PARAM;
        }

        DoorOpenCloseOperation operation = param.toOperation();

        int msgCode = openDoor ? Constants.CODE_DOOR_OPEN : Constants.CODE_DOOR_CLOSE;
        DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, msgCode);
        log.info("[远程开关门操作] - {}:{}-{}", retCode.getCode(), retCode.getMsg(), param);

        return retCode;
    }

    /**
     * 参数配置
     * @param sn
     */
    public void deviceSitting(String sn) {
        // 监控开启
        NoBodyOperation operation = new NoBodyOperation();
        DeviceResopnseType retCode = tcpServer.deviceSetting(sn, operation, Constants.CODE_MONITOR_ON);
        log.info("[实时监控开启] - {}:{}", retCode.getCode(), retCode.getMsg());

        // 设置读卡器字节数
        List<PortConfigInfo> portConfigInfos = portService.getReaderBytes(sn);
        log.info("[读卡器配置信息] - {}", portConfigInfos);
        ReaderByteOperation readerByteOperation = new ReaderByteOperation();
        for (PortConfigInfo port:portConfigInfos) {
            int doorNo = port.getPortNo();
            int readerByte = port.getReaderByte();
            switch (doorNo) {
                case 0:
                    readerByteOperation.setPort1(readerByte);
                    break;
                case 1:
                    readerByteOperation.setPort2(readerByte);
                    break;
                case 2:
                    readerByteOperation.setPort3(readerByte);
                    break;
                case 3:
                    readerByteOperation.setPort4(readerByte);
                    break;
                default:
                    break;
            }
        }

        DeviceResopnseType retCode1 =
                tcpServer.deviceSetting(sn, readerByteOperation, OperationType.READER_BYTES.getSettingCode());
        log.info("[端口读卡器字节数配置] - {}:{}", retCode1.getCode(), retCode1.getMsg());


        // 时间组下载
        ScpTimeSetInfo info = employeeDoorService.getScpTimeSetList(sn);
        if (Objects.isNull(info)) {
            log.info("无时间组需要配置：{}");
        } else {
            sendScpTimeSet(info);
        }
    }

}
