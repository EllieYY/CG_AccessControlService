package com.wimetro.cg.model.response;

public enum DeviceResopnseType {
    SUCCESS(0, "命令发送成功"),
    INVALID_DEVICE(1001, "设备不存在"),
    DEVICE_UNREACHABLE(1002, "设备超时响应或连接断开"),
    INVALID_PARAM(1003, "参数非法"),
    INVALID_MSG(1004, "指令错误");

    private int code;
    private String msg;

    DeviceResopnseType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
