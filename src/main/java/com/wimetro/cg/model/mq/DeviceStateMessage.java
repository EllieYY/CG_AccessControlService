package com.wimetro.cg.model.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @title: DeviceStateMessage
 * @author: Ellie
 * @date: 2023/08/04 09:52
 * @description:
 **/
@Data
@AllArgsConstructor
public class DeviceStateMessage {
    private String ip;              // 控制器IP
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventsTime;         // 事件时间
    private int state;              // 控制器状态 0-离线 1-在线
}
