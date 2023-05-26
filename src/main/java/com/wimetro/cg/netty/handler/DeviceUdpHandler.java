package com.wimetro.cg.netty.handler;

import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.netty.runner.RequestPendingCenter;
import com.wimetro.cg.protocol.NetIdentifierOperation;
import com.wimetro.cg.protocol.TcpParamOperation;
import com.wimetro.cg.protocol.TcpParamOperationResult;
import com.wimetro.cg.protocol.message.DeviceMessage;
import com.wimetro.cg.protocol.message.MessageHeader;
import com.wimetro.cg.protocol.message.ServerMessage;
import com.wimetro.cg.service.DeviceCenter;
import com.wimetro.cg.util.IdUtil;
import com.wimetro.cg.util.NetUtil;
import com.wimetro.cg.util.ToolConvert;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @title: DeviceUdpHandler
 * @author: Ellie
 * @date: 2023/03/22 09:13
 * @description:
 **/
@Slf4j
public class DeviceUdpHandler extends SimpleChannelInboundHandler<DeviceMessage> {

    private IdUtil idUtil;
    private NettyConfig nettyConfig;
    public DeviceUdpHandler(NettyConfig nettyConfig, IdUtil idUtil) {
        this.idUtil = idUtil;
        this.nettyConfig = nettyConfig;
    }



    @Override
    public void channelRead0(ChannelHandlerContext ctx, DeviceMessage deviceMessage) throws Exception {
//        log.info("{}", deviceMessage);

        MessageHeader deviceMsgHeader = deviceMessage.getMessageHeader();
        int streamId = deviceMsgHeader.getStreamId();
        RequestPendingCenter.set(streamId, deviceMessage);

        /** 响应处理 */
        int msgCode = deviceMsgHeader.getMsgCode();
        if (msgCode == 0x31FE00) {
            /** 设置设备网络标识 */
            setNetIdentifier(ctx, deviceMessage.getSenderAddress(), deviceMsgHeader);

//            /** 设置Tcp参数 */
//            setTcpParam(ctx, deviceMessage.getSenderAddress(), deviceMessage);

            /** 存储搜索到的设备信息 */
            TcpParamOperationResult body = (TcpParamOperationResult) deviceMessage.getMessageBody();
            TcpParamOperation operation = body.toOperation();
            DeviceCenter.addDevice(deviceMsgHeader.getDeviceSN(), deviceMsgHeader.getDevicePwd(),
                    operation, idUtil.getNetIdentifier());
        }
    }

    /**
     * 设置网络标识
     * @param ctx
     * @param deviceMsgHeader
     */
    private void setNetIdentifier(ChannelHandlerContext ctx, InetSocketAddress targetAddress, MessageHeader deviceMsgHeader) {
        MessageHeader header = new MessageHeader();
        header.setMsgCode(0x01FE01);
        header.setDeviceSN(deviceMsgHeader.getDeviceSN());
        header.setDevicePwd(deviceMsgHeader.getDevicePwd());
        header.setStreamId(IdUtil.randomRangeInt(0, 100000));
        NetIdentifierOperation body = new NetIdentifierOperation(idUtil.getNetIdentifier());


        ServerMessage serverMessage = new ServerMessage(header, body);
        serverMessage.setTargetAddress(targetAddress);
        log.info("[设置网络标识] {} - {}", targetAddress, serverMessage);
        ctx.write(serverMessage);
    }

    /**
     * 设置设备Tcp参数
     * - 设备作为client连接本机的Tcp服务器
     * @param ctx
     * @param targetAddress
     * @param deviceMessage
     */
    private void setTcpParam(ChannelHandlerContext ctx, InetSocketAddress targetAddress, DeviceMessage deviceMessage) {
        MessageHeader deviceMsgHeader = deviceMessage.getMessageHeader();
        MessageHeader header = new MessageHeader();
        header.setMsgCode(0x010601);
        header.setDeviceSN(deviceMsgHeader.getDeviceSN());
        header.setDevicePwd(deviceMsgHeader.getDevicePwd());
        header.setStreamId(IdUtil.randomRangeInt(0, 100000));

        TcpParamOperationResult deviceMsgBody = (TcpParamOperationResult)deviceMessage.getMessageBody();
        // 参数组装
        String hostIp = NetUtil.getLocalHostIp();
        TcpParamOperation body = new TcpParamOperation();
        body.setMac(deviceMsgBody.getMac());
        body.setIp(ToolConvert.ip2ip(deviceMsgBody.getIp(), hostIp));
        body.setMask(deviceMsgBody.getMask());
        body.setGateway(ToolConvert.ip2gateway(hostIp));
        body.setDns(deviceMsgBody.getDns());
        body.setAltDns(deviceMsgBody.getAltDns());
        body.setTcpMode(1);   // tcp client 模式
        body.setTcpPort(deviceMsgBody.getTcpPort());
        body.setUdpPort(deviceMsgBody.getUdpPort());
        body.setTargetIp(NetUtil.getLocalHostIp());   // 本机ip
        body.setTargetPort(nettyConfig.getTcpServerPort());  //
        body.setAutoIp(deviceMsgBody.getAutoIp());
        body.setDomain(deviceMsgBody.getDomain());

        ServerMessage serverMessage = new ServerMessage(header, body);
        serverMessage.setTargetAddress(targetAddress);
        log.info("[设置设备Tcp参数] {} - {}", targetAddress, serverMessage);
        ctx.write(serverMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        log.error("出现异常",cause);
    }
}
