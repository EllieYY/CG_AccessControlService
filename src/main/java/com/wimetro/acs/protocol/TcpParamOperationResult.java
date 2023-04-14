package com.wimetro.acs.protocol;

import com.wimetro.acs.model.device.TcpInfo;
import com.wimetro.acs.protocol.message.Operation;
import com.wimetro.acs.protocol.message.OperationResult;
import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @title: TcpParam
 * @author: Ellie
 * @date: 2023/03/17 14:17
 * @description:
 **/
@Data
@Slf4j
public class TcpParamOperationResult extends OperationResult {
    @CmdProp(index = 0, len = 6, deCodec = "bytesToMac")
    private String mac;

    @CmdProp(index = 1, len = 4, deCodec = "bytesToIp")
    private String ip;

    @CmdProp(index = 2, len = 4, deCodec = "bytesToIp")
    private String mask;

    @CmdProp(index = 3, len = 4, deCodec = "bytesToIp")
    private String gateway;

    @CmdProp(index = 4, len = 4, deCodec = "bytesToIp")
    private String dns;

    @CmdProp(index = 5, len = 4, deCodec = "bytesToIp")
    private String altDns;

    @CmdProp(index = 6, len = 1, deCodec = "bytesToInt")
    private int tcpMode;

    @CmdProp(index = 7, len = 2, deCodec = "bytesToInt")
    private int tcpPort;

    @CmdProp(index = 8, len = 2, deCodec = "bytesToInt")
    private int udpPort;

    @CmdProp(index = 9, len = 2, deCodec = "bytesToInt")
    private int targetPort;

    @CmdProp(index = 10, len = 4, deCodec = "bytesToIp")
    private String targetIp;

    @CmdProp(index = 11, len = 1, deCodec = "bytesToInt")
    private int autoIp;

    @CmdProp(index = 12, len = 99, deCodec = "bytesToStr")
    private String domain;


    public TcpParamOperation toOperation() {
        TcpParamOperation result = new TcpParamOperation();
        result.setMac(mac);
        result.setIp(ip);
        result.setMask(mask);
        result.setGateway(gateway);
        result.setDns(dns);
        result.setAltDns(altDns);
        result.setTcpMode(tcpMode);
        result.setTcpPort(tcpPort);
        result.setUdpPort(udpPort);
        result.setTargetPort(targetPort);
        result.setTargetIp(targetIp);
        result.setAutoIp(autoIp);
        result.setDomain(domain);

        return result;
    }

    public TcpInfo toTcpInfo(String pwd) {
        TcpInfo result = new TcpInfo();
        result.setMac(mac);
        result.setIp(ip);
        result.setMask(mask);
        result.setGateway(gateway);
        result.setTcpPort(tcpPort);
        result.setPassword(pwd);

        return result;
    }

}
