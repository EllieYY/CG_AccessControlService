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

    int CODE_CARD_DELETE = 0x070500;
    int CODE_SORT_CARD_START = 0x070700;
    int CODE_SORT_CARD_END = 0x070702;

    int CODE_CARD_CAPACITY = 0x070100;
    int CODE_FUN_PARAM = 0x010AFF;
    int CODE_OPS_PARAM = 0x010900;

    int CODE_PORT_INFO = 0x010E00;
    int CODE_RELAY_OUT = 0x030200;  // 继电器输出模式
    int CODE_READER_BYTES = 0x030100; // 读卡器字节数
    int CODE_FORCED_PWD = 0x030B01;   // 胁迫报警密码

    // 空闲检测参数
    int SERVER_READ_IDEL_TIME_OUT = 5;    // 空闲检测间隔，单位秒
    int SERVER_WRITE_IDEL_TIME_OUT = 0;
    int SERVER_ALL_IDEL_TIME_OUT = 0;
    int MAX_LOSS_CONNECT_TIME = 3;    // 最大连续失联次数

    // 报文结构信息
    int MSG_PREFIX_LENGTH = 16;

    int NO_RESPONSE_CODE = -1;     // 不做回复的操作码

}
