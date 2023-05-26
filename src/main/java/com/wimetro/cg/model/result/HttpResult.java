package com.wimetro.cg.model.result;

/**
 * @Description : rest template 返回结果封装
 * @Author : Ellie
 * @Date : 2019/2/19
 */
public class HttpResult {
    private Integer code;
    private String body;

    public HttpResult(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return code + ":" + body;
    }
}
