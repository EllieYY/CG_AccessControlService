package com.wimetro.acs.protocol.message;

import com.wimetro.acs.util.ToolConvert;
import lombok.Data;

/**
 * @title: MessageHeader
 * @author: Ellie
 * @date: 2023/03/10 11:26
 * @description:
 **/
@Data
public class MessageHeader {
    private int streamId;       // 4byte 信息代码-对应协议说法
    private String deviceSN;    // 16byte 设备sn
    private String devicePwd;   // 4byte 密码-用于加密通信

    private int msgCode;        // 3byte 控制码

    private int length;         // 4byte 数据长度

    static int HEAD_LEN = 31;   // 报文头长度

    @Override
    public String toString() {
        return "MessageHeader{" +
                "streamId=" + streamId +
                ", deviceSN='" + deviceSN + '\'' +
                ", devicePwd='" + devicePwd + '\'' +
                ", msgCode=0x" + ToolConvert.intToHexStr(msgCode) +
                ", length=" + length + "(0x" + ToolConvert.intToHexStr(length) + ")" +
                '}';
    }

    /**
     * PC发送信息头：设备SN（16byte） | 密码（4byte） | 信息代码（4byte） | 控制码（3byte） | 数据长度（4byte）
     * @return
     */
    public String encode() {
        String headStr = "";
        headStr += ToolConvert.strToGBKHexStr(1, 16, deviceSN);
        headStr += devicePwd;
        headStr += ToolConvert.intToHexStr(1, 4, streamId);
        headStr += ToolConvert.intToHexStr(1, 3, msgCode);
        headStr += ToolConvert.intToHexStr(1, 4, length);

        return headStr;
    }
}
