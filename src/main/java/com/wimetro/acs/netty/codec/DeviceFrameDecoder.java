package com.wimetro.acs.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;

/**
 * @title: DeviceFrameDecoder
 * @author: Ellie
 * @date: 2023/03/10 10:45
 * @description: ByteBuf转byte
 **/
@Slf4j
public class DeviceFrameDecoder extends DelimiterBasedFrameDecoder {

    public DeviceFrameDecoder(int maxFrameLength, ByteBuf delimiter) {
        super(maxFrameLength, delimiter);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, buffer);
        if (buf == null) {
            return null;
        }
        if (buf.capacity() == 0) {
            buf.release();
            return null;
        }
        try {
            int len = buf.readableBytes();
            byte[] readableByte = new byte[len];
            buf.getBytes(0, readableByte);
            buf.release();
            return readableByte;
        } catch (Exception e) {
            log.error("处理消息分隔符时发生异常", e);
        } finally {
            if (buf.refCnt() > 0) {
                buf.release();
            }
        }
        return null;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
