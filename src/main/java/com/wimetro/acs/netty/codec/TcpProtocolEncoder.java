package com.wimetro.acs.netty.codec;


import com.wimetro.acs.protocol.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @title: ProtocolEncoder
 * @author: Ellie
 * @date: 2022/02/10 16:42
 * @description:
 **/
@Slf4j
public class TcpProtocolEncoder extends MessageToMessageEncoder<ServerMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ServerMessage ServerMessage, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        ServerMessage.encode(buffer);

        out.add(buffer);
    }
}
