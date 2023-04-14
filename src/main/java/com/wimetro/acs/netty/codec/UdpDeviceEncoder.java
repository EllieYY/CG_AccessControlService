package com.wimetro.acs.netty.codec;

import com.wimetro.acs.protocol.message.DeviceMessage;
import com.wimetro.acs.protocol.message.ServerMessage;
import com.wimetro.acs.util.ToolConvert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @title: ProtocolDecoder
 * @author: Ellie
 * @date: 2022/02/10 11:25
 * @description:
 **/
@Slf4j
public class UdpDeviceEncoder extends MessageToMessageEncoder<ServerMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ServerMessage serverMessage, List<Object> list) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        serverMessage.encode(buffer);

        list.add(new DatagramPacket(buffer, serverMessage.getTargetAddress()));
    }
}
