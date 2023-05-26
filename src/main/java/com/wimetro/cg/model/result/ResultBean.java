package com.wimetro.cg.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description : 返回对象实体
 * @Author : Ellie
 * @Date : 2019/2/25
 */
@Data
@NoArgsConstructor
@ApiModel(value = "通用接口返回")
public class ResultBean<T> {
    @ApiModelProperty(value = "状态码", required = true)
    @JsonProperty("iret")
    private int code;

    @ApiModelProperty(value = "返回信息", required = true)
    @JsonProperty("msg")
    private String msg;

    @ApiModelProperty(value = "返回数据", required = true)
    @JsonProperty("data")
    private T data;

    public ResultBean(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public ResultBean(RespCode respCode, T data) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.data = data;
    }

    public ResultBean(RespCode respCode, String msg) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg() + ": " + msg;
        this.data = data;
    }

    public ResultBean(Throwable e) {
        this.code = RespCode.FAIL.getCode();
        this.msg = e.toString();
    }
}
