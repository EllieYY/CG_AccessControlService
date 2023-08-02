package com.wimetro.cg.protocol.events;

/**
 * 设备事件接口
 */
public interface DeviceEvent {
    void sendMq(String sn);
}
