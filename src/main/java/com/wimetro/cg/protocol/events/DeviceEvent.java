package com.wimetro.cg.protocol.events;

import com.wimetro.cg.service.QueueProducer;

/**
 * 设备事件接口
 */
public interface DeviceEvent {
    void sendMq(QueueProducer queueProducer, String sn);
}
