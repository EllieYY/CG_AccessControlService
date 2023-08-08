package com.wimetro.cg.common;

/**
 * @description 常量定义
 */
public interface Constants {
    String ENCODER_TO_STR = "toStr";
    String IP_PORT_SPLITTER = ":";

    int BATCH_CARD_DATA_COUNT = 10;

    int CODE_BROADCAST = 0x01FE00;
    int CODE_OK = 0x210100;
    int CODE_TCP_PARAM_READ = 0x010600;

    int CODE_CARD_CLEAR = 0x070200;
    int CODE_CARD_DELETE = 0x070500;
    int CODE_SORT_CARD_START = 0x070700;
    int CODE_SORT_CARD_END = 0x070702;
    int CODE_TIMESET_CLEAR = 0x060100;

    int CODE_DEVICE_INIT = 0x010F00;

    // 开关门
    int CODE_DOOR_OPEN = 0x030300;
    int CODE_DOOR_CLOSE = 0x030301;

    // 监控开启
    int CODE_MONITOR_ON = 0x010B00;

    // 事件
    /**1:读卡记录;
     * 2：出门开关记录;
     * 3:门磁记录;
     * 4:软件操作记录;
     * 5:报警记录;
     * 6:系统记录
     */
    int EVENT_CARD = 1;
    int EVENT_DOOR = 2;
    int EVENT_STRIKE = 3;
    int EVENT_OPERATION = 4;
    int EVENT_ALARM = 5;
    int EVENT_SYSTEM = 6;

    // 空闲检测参数
    int SERVER_READ_IDEL_TIME_OUT = 5;    // 空闲检测间隔，单位秒
    int SERVER_WRITE_IDEL_TIME_OUT = 0;
    int SERVER_ALL_IDEL_TIME_OUT = 0;
    int MAX_LOSS_CONNECT_TIME = 3;    // 最大连续失联次数

    // 报文结构信息
    int MSG_PREFIX_LENGTH = 16;

    int NO_RESPONSE_CODE = -1;     // 不做回复的操作码

}
