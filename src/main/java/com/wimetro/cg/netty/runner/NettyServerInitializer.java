package com.wimetro.cg.netty.runner;

import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.netty.codec.*;
import com.wimetro.cg.netty.handler.DeviceTcpHandler;
import com.wimetro.cg.service.QueueProducer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @title: NettyServerInitializer
 * @author: Ellie
 * @date: 2022/02/24 20:56
 * @description:
 **/
@Slf4j
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    UnorderedThreadPoolEventExecutor businessGroup = null;
    private NettyConfig nettyConfig;
    private QueueProducer queueProducer;

    public NettyServerInitializer(UnorderedThreadPoolEventExecutor businessGroup, NettyConfig nettyConfig, QueueProducer queueProducer) {
        this.businessGroup = businessGroup;
        this.nettyConfig = nettyConfig;
        this.queueProducer = queueProducer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

//        pipeline.addLast(new IdleStateHandler(
//                Constants.SERVER_READ_IDEL_TIME_OUT,
//                Constants.SERVER_WRITE_IDEL_TIME_OUT,
//                Constants.SERVER_ALL_IDEL_TIME_OUT,
//                TimeUnit.SECONDS));
//        pipeline.addLast(new AcceptorIdleStateTrigger(nettyConfig));
        pipeline.addLast(new DeviceFrameDecoder(10 * 1024, Unpooled.copiedBuffer(new byte[]{0x7E})));
        pipeline.addLast("deviceProtocolDecoder", new TcpProtocolDecoder());
        pipeline.addLast("deviceProtocolEncoder", new TcpProtocolEncoder());

        pipeline.addLast("idleCheck", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(businessGroup,"processHandler", new DeviceTcpHandler(nettyConfig, queueProducer));
    }
}
