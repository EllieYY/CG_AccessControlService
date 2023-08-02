package com.wimetro.cg.netty.runner;

import com.wimetro.cg.common.Constants;
import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.model.device.DeviceShadow;
import com.wimetro.cg.protocol.TcpParamOperation;
import com.wimetro.cg.protocol.message.*;
import com.wimetro.cg.service.DeviceCenter;
import com.wimetro.cg.util.IdUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
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
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @title: NettyTcpServer
 * @author: Ellie
 * @date: 2023/02/24 20:54
 * @description:
 **/
@Slf4j
@Component
@EnableAsync
public class NettyTcpServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup = null;
    UnorderedThreadPoolEventExecutor businessGroup = null;
    ChannelFuture deviceFuture = null;

    // linux平台下可开启
    boolean epoll = false;

    private ApplicationContext applicationContext;

    private final NettyConfig nettyConfig;
    public NettyTcpServer(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
    }


    @PreDestroy
    public void stop() {
        if(deviceFuture !=null) {
            deviceFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
            deviceFuture.awaitUninterruptibly();
            deviceFuture = null;
        }

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        businessGroup.shutdownGracefully();
        log.info(" 服务关闭 ");
    }

    public void start() {
        log.info(" TcpServer 正在启动");

        // thread
        if(epoll) {
            log.info(" TcpServer 使用epoll模式");
            bossGroup = new EpollEventLoopGroup(0, new DefaultThreadFactory("boss"));
            workerGroup = new EpollEventLoopGroup(0, new DefaultThreadFactory("worker"));
        } else {
            log.info(" TcpServer 使用nio模式");
            bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
            workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        }
        businessGroup = new UnorderedThreadPoolEventExecutor(
                10, new DefaultThreadFactory("business"));

        // log
        LoggingHandler debugLogHandler = new LoggingHandler(LogLevel.DEBUG);
        LoggingHandler infoLogHandler = new LoggingHandler(LogLevel.INFO);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // option
        if(epoll) {
            serverBootstrap.channel(EpollServerSocketChannel.class);
            serverBootstrap.option(EpollChannelOption.SO_REUSEADDR, true);
        } else {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(NioChannelOption.SO_REUSEADDR, true);
        }


        serverBootstrap.option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
//                .handler(infoLogHandler)
                .childHandler(new NettyServerInitializer(businessGroup, nettyConfig));

        // 端口监听
        try {
            serverBootstrap.group(bossGroup, workerGroup);
            deviceFuture = serverBootstrap.bind(nettyConfig.getTcpServerPort()).sync();
            log.info("tcp server listened on " + deviceFuture.channel().localAddress());

//            // 同步等待channel关闭
//            deviceFuture.channel().closeFuture().sync();

            // 异步等待channel关闭
            deviceFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override public void operationComplete(ChannelFuture future) throws Exception {       //通过回调只关闭自己监听的channel
                    future.channel().close();
                }
            });

        } catch (Exception e){
            log.info("nettyServer 启动时发生异常,{}",e);
            log.info(e.getMessage());
        } finally {
            // 异步等待时注释下面关闭操作
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            businessGroup.shutdownGracefully();
        }
    }


    /**
     * 获取设备信息
     * @return
     */
    public OperationResult sendDeviceInfo(String sn, Operation body, int msgCode) {
//        int msgCode = operationType.getReadCode();
        // n - 查找设备ip | password | port
        DeviceShadow deviceShadow = DeviceCenter.getDevice(sn);
        if (Objects.isNull(deviceShadow)) {
            log.info("设备 {} 不存在", sn);
            return null;
        }

        TcpParamOperation tcpParam = deviceShadow.getTcpParam();
        String ip = tcpParam.getIp();
        String pwd = deviceShadow.getPassword();
        int deviceTcpPort = tcpParam.getTcpPort();

        // 报文头组装
        MessageHeader header = new MessageHeader();
        header.setMsgCode(msgCode);
        header.setDeviceSN(sn);
        header.setDevicePwd(pwd);
        int streamId = IdUtil.randomRangeInt(0, 100000);
        header.setStreamId(streamId);

        // 报文组装
        ServerMessage serverMessage = new ServerMessage(header, body);

        // 消息发送和等待
        Future<DeviceMessage> future = RequestPendingCenter.add(streamId);

        // 查找设备channel
        String key = ip + Constants.IP_PORT_SPLITTER + nettyConfig.getTcpServerPort();
        Channel channel = ChannelManager.getChannel(key);
        if (Objects.isNull(channel)) {
            log.info("设备连接通道已断开 {} - {}", ip, sn);
            return null;
        }
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        serverMessage.setTargetAddress(remoteAddress);
        tcpSend(serverMessage, channel);

        try {
            // 获取结果，超时时间3秒
            DeviceMessage response = future.get(10, TimeUnit.SECONDS);
            return response.getMessageBody();
        } catch (Exception e) {
            log.error("{}", e);
        }

        return null;
    }



    /**
     * 发送消息
     * @param serverMessage
     * @param channel
     */
    public void tcpSend(final ServerMessage serverMessage, Channel channel) {
        if (channel.isActive() && channel.isWritable()) {
            try {
                channel.writeAndFlush(serverMessage).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            log.error(serverMessage.toString() + "发送失败");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                log.error("tcpSend()调用异常：", e);
            }
        } else {
            log.error("not writable now, message dropped");
        }
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
