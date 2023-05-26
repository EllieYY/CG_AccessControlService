package com.wimetro.cg.netty.runner;

import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.netty.codec.UdpDeviceDecoder;
import com.wimetro.cg.netty.codec.UdpDeviceEncoder;
import com.wimetro.cg.netty.handler.DeviceUdpHandler;
import com.wimetro.cg.protocol.message.ServerMessage;
import com.wimetro.cg.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @title: NettyUdpServer
 * @author: Ellie
 * @date: 2023/02/24 20:54
 * @description:
 **/
@Slf4j
@Component
@EnableAsync
public class NettyUdpServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {
    private EventLoopGroup bossGroup = null;
    private ChannelFuture deviceFuture = null;

    // linux平台下可开启
    boolean epoll = false;
    private ApplicationContext applicationContext;
    private final NettyConfig nettyConfig;
    private final IdUtil idUtil;
    public NettyUdpServer(NettyConfig nettyConfig, IdUtil idUtil) {
        this.nettyConfig = nettyConfig;
        this.idUtil = idUtil;
    }


    @PreDestroy
    public void stop() {
        if(deviceFuture !=null) {
            deviceFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
            deviceFuture.awaitUninterruptibly();
            deviceFuture = null;
        }

        bossGroup.shutdownGracefully();
        log.info(" 服务关闭 ");
    }

    public void start() {
        log.info(" UdpServer 正在启动");

        // thread
        if(epoll) {
            log.info(" UdpServer 使用epoll模式");
            bossGroup = new EpollEventLoopGroup(0, new DefaultThreadFactory("boss"));
        } else {
            log.info(" UdpServer 使用nio模式");
            bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        }

        // log
        LoggingHandler debugLogHandler = new LoggingHandler(LogLevel.DEBUG);
        LoggingHandler infoLogHandler = new LoggingHandler(LogLevel.INFO);

        Bootstrap bootstrap = new Bootstrap();
        // option
        // 设置NIO UDP连接通道，设置通道参数 SO_BROADCAST广播形式
        if(epoll) {
            bootstrap.channel(EpollDatagramChannel.class);
            bootstrap.option(EpollChannelOption.SO_BROADCAST, true);
        } else {
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.option(NioChannelOption.SO_BROADCAST, true);
        }

        bootstrap.group(bossGroup);
        bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch)
                    throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new UdpDeviceDecoder());    // frame和协议解析
                pipeline.addLast(new UdpDeviceEncoder());    //
                pipeline.addLast(new DeviceUdpHandler(nettyConfig, idUtil));    // 业务处理
            }
        });

        // 端口监听
        try {
            // 绑定server，通过调用sync（）方法异步阻塞，直到绑定成功
            deviceFuture = bootstrap.bind(nettyConfig.getUdpServerPort()).sync();
            log.info("udp server listened on " + deviceFuture.channel().localAddress());

//            // 同步等待channel关闭
//            deviceFuture.channel().closeFuture().sync();

            // 异步等待channel关闭
            deviceFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override public void operationComplete(ChannelFuture future) throws Exception {       //通过回调只关闭自己监听的channel
                    future.channel().close();
                }
            });

        } catch (Exception e){
            log.info("UdpServer 启动时发生异常,{}",e);
            log.info(e.getMessage());
        } finally {
            // 异步等待时注释下面关闭操作
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            businessGroup.shutdownGracefully();
        }
    }

    /**
     * UDP发送，是否是广播信息，取决于message的目标地址
     * @param message
     */
    public void udpSend(ServerMessage message) {
        deviceFuture.channel().writeAndFlush(message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.stop();
        log.info("服务停止");
    }
}
