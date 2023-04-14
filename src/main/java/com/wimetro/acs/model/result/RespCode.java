package com.wimetro.acs.model.result;


public enum RespCode {
    SUCCESS(0, "请求成功"),
    FAIL(9009, "未知错误"),
    CMD_DOWNLOAD_FAIL(2001, "命令下发失败"),
    COMM_SERVICE_FAIL(2002, "通信服务报错"),
    INVALID_PARAM(1001, "无效参数"),
    ERROR_PARAM(1002, "错误参数"),
    INNER_ERR(9001, "服务内部错误");

    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
