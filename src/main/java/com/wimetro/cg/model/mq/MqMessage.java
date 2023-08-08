package com.wimetro.cg.model.mq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @title: AccessMessage
 * @author: Ellie
 * @date: 2022/06/13 11:13
 * @description:
 **/
@Data
@AllArgsConstructor
public class MqMessage {
    /**
     1:读卡记录;2：出门开关记录;3:门磁记录;4:软件操作记录;5:报警记录;6:系统记录
     */
    private int recordType;          // 记录类型
    private String sn;              // 控制器ID
    private int portNo;            // 端口号 | 门号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventsTime;         // 事件时间
    private int eventCode;             // 设备事件码
    private String cardNo;             // 刷卡事件卡号
}
