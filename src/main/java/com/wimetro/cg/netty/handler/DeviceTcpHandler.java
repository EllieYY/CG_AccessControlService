package com.wimetro.cg.netty.handler;

import com.wimetro.cg.common.*;
import com.wimetro.cg.config.NettyConfig;
import com.wimetro.cg.model.mq.DeviceStateMessage;
import com.wimetro.cg.netty.runner.ChannelManager;
import com.wimetro.cg.netty.runner.RequestPendingCenter;
import com.wimetro.cg.protocol.NoBodyOperation;
import com.wimetro.cg.protocol.events.DeviceEvent;
import com.wimetro.cg.protocol.message.*;
import com.wimetro.cg.service.QueueProducer;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Objects;

/**
 * @title: DeviceTcpHandler
 * @author: Ellie
 * @date: 2023/02/10 15:54
 * @description:
 **/
@Slf4j
@ChannelHandler.Sharable
public class DeviceTcpHandler extends SimpleChannelInboundHandler<DeviceMessage> {

    private NettyConfig nettyConfig;
    private QueueProducer queueProducer;

    public DeviceTcpHandler(NettyConfig nettyConfig, QueueProducer queueProducer) {
        this.nettyConfig = nettyConfig;
        this.queueProducer = queueProducer;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DeviceMessage deviceMessage) throws Exception {
        MessageHeader msgHeader = deviceMessage.getMessageHeader();

        // 响应分发
        int streamId = msgHeader.getStreamId();
        RequestPendingCenter.set(streamId, deviceMessage);

        // 设备连接确认
        int opCode = msgHeader.getMsgCode();
        if (opCode == OperationType.CONNECT_CONFIRM.getResponseCode()) {
            ChannelManager.registry(ctx.channel());
        }

        if (opCode == OperationType.CONNECT_TEST.getResponseCode()) {
            // MQ事件推送
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = remoteAddress.getAddress().getHostAddress();
            queueProducer.sendStatusMessage(new DeviceStateMessage(ip, new Date(), 1));
        }

        // 事件上传MQ
        MessageBody body = deviceMessage.getMessageBody();
        if (body instanceof DeviceEvent) {
            ((DeviceEvent) body).sendMq(queueProducer, msgHeader.getDeviceSN());
        }

        // 默认回复ok
//        ServerMessage serverMessage = makeOkResp(msgHeader);

        // 获取发送channel
        String key = ChannelManager.getChannelKey(ctx.channel());
        Channel targetChannel = ChannelManager.getChannel(key);
        if (Objects.isNull(targetChannel)) {
            log.error("[发送目标未注册] - {}", key);
            return;
        }

        // TODO：设备监控消息处理

        // 消息发送
//        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
//        serverMessage.setTargetAddress(remoteAddress);
//        writeToClient(serverMessage, targetChannel);
    }

    /**
     * 回复ok报文
     */
    private ServerMessage makeOkResp(MessageHeader msgHeader) {
        MessageHeader header = new MessageHeader();
        header.setMsgCode(Constants.CODE_OK);
        header.setDeviceSN(msgHeader.getDeviceSN());
        header.setDevicePwd(msgHeader.getDevicePwd());
        header.setStreamId(msgHeader.getStreamId());

        NoBodyOperation body = new NoBodyOperation();
        ServerMessage serverMessage = new ServerMessage(header, body);

        return serverMessage;
    }


    private void writeToClient(final ServerMessage serverMessage, Channel channel) {
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
                log.error("调用通用writeToClient()异常：", e);
            }
        } else {
            log.error("not writable now, message dropped");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        Channel ch = ctx.channel();
        log.info("[连接成功] <- {}", ch.remoteAddress().toString().substring(1));
        ChannelManager.addChannelToGroup(ch);
        ChannelManager.registry(ch);

        // MQ事件推送
        InetSocketAddress remoteAddress = (InetSocketAddress) ch.remoteAddress();
        String ip = remoteAddress.getAddress().getHostAddress();
        queueProducer.sendStatusMessage(new DeviceStateMessage(ip, new Date(), 1));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        // 连接关系维护
        Channel ch = ctx.channel();
        ChannelManager.logout(ch);
        ChannelManager.removeChannelFromGroup(ch);
        log.info("[断开连接] !- {}", ch.remoteAddress());

        // MQ事件推送
        InetSocketAddress remoteAddress = (InetSocketAddress) ch.remoteAddress();
        String ip = remoteAddress.getAddress().getHostAddress();
        queueProducer.sendStatusMessage(new DeviceStateMessage(ip, new Date(), 0));

        // 连接状态通知
        InetSocketAddress serverSocket = (InetSocketAddress)ch.localAddress();
        int localPort = serverSocket.getPort();
        // 设备断开需要通知
        if (localPort == nettyConfig.getTcpServerPort()) {
            //TODO: 客户端断开通知
//        HttpUtil.syncNetworkStatus(clientIp, 0);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        cause.printStackTrace();
        Channel ch = ctx.channel();

        ChannelManager.logout(ch);
        ChannelManager.removeChannelFromGroup(ch);

        // MQ事件推送
        InetSocketAddress remoteAddress = (InetSocketAddress) ch.remoteAddress();
        String ip = remoteAddress.getAddress().getHostAddress();
        queueProducer.sendStatusMessage(new DeviceStateMessage(ip, new Date(), 0));

        if (ch.isActive()) {
            ch.close();
        }
        log.error("{} -> [异常]原因：{}", ch.remoteAddress().toString(),
                cause.getMessage());
    }
}
