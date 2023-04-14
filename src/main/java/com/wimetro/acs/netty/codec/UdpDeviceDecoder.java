package com.wimetro.acs.netty.codec;

import com.wimetro.acs.common.Constants;
import com.wimetro.acs.protocol.message.DeviceMessage;
import com.wimetro.acs.util.NetUtil;
import com.wimetro.acs.util.ToolConvert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * @title: ProtocolDecoder
 * @author: Ellie
 * @date: 2022/02/10 11:25
 * @description:
 **/
@Slf4j
public class UdpDeviceDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        final ByteBuf buf = datagramPacket.content();
        int readableBytes = buf.readableBytes();
        byte[] content = new byte[readableBytes - 2];

        // skip 报文头7e和报文尾7e
        buf.skipBytes(1);
        buf.readBytes(content);
        String contentHexStr = ToolConvert.bytesToHexStr(content);
//        log.info("[解析]{} => {}", datagramPacket.sender(), contentHexStr);

        // 设备信息
        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setSenderAddress(datagramPacket.sender());
        if (deviceMessage.decode(content)) {
            list.add(deviceMessage);
        } else {
            // 过滤广播消息
            InetAddress localAddress = NetUtil.getLocalHostExactAddress();
            InetAddress senderAddress = datagramPacket.sender().getAddress();
            if (localAddress.equals(senderAddress)) {
                return;
            }
            log.error("[解析失败] {} => [{}]", datagramPacket.sender(), contentHexStr);
        }
    }

}
