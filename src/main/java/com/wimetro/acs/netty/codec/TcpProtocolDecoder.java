package com.wimetro.acs.netty.codec;

import com.wimetro.acs.protocol.message.DeviceMessage;
import com.wimetro.acs.util.ToolConvert;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @title: ProtocolDecoder
 * @author: Ellie
 * @date: 2022/02/10 11:25
 * @description:
 **/
@Slf4j
public class TcpProtocolDecoder extends MessageToMessageDecoder<Object> {
//    private String webKey;

//    public DeviceProtocolDecoder() {
//        this.webKey = webKey;
//    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Object byteMsg, List<Object> out) throws Exception {
        String hexCmd = ToolConvert.bytesToHexStr((byte[])byteMsg);
        log.info("[device报文]{}", hexCmd);

        Channel ch = ctx.channel();

        DeviceMessage acsRequestMessage = new DeviceMessage();
        acsRequestMessage.setSenderAddress((InetSocketAddress) ch.remoteAddress());
        if (acsRequestMessage.decode(byteMsg)) {
            out.add(acsRequestMessage);
        } else {
            log.error("[无法解析报文] - [{}]", hexCmd);
        }
    }
}
