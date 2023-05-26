package com.wimetro.cg.model.result;

/**
 * @Description : 封装成result bean工具
 * @Author : Ellie
 * @Date : 2019/2/25
 */
public class ResultBeanUtil {
    private final static String SUCCESS = "success";

    public static <T> ResultBean<T> makeOkResp() {
        return new ResultBean<T>(RespCode.SUCCESS);
    }

    public static <T> ResultBean<T> makeOkResp(T data) {
        return new ResultBean<T>(RespCode.SUCCESS, data);
    }

    public static <T> ResultBean<T> makeInnerErrResp() {
        return new ResultBean<T>(RespCode.INNER_ERR);
    }

    public static <T> ResultBean<T> makeParamErrResp() {
        return new ResultBean<T>(RespCode.ERROR_PARAM);
    }

    public static <T> ResultBean<T> makeCustomErrResp(String msg) {
        RespCode.FAIL.setMsg(msg);
        return new ResultBean<T>(RespCode.FAIL);
    }

    public static <T> ResultBean<T> makeParamErrResp(String msg) {
        RespCode.ERROR_PARAM.setMsg(msg);
        return new ResultBean<T>(RespCode.ERROR_PARAM);
    }

    public static <T> ResultBean<T> makeParamInvalidResp(String msg) {
        RespCode.INVALID_PARAM.setMsg(msg);
        return new ResultBean<T>(RespCode.INVALID_PARAM);
    }

    public static <T> ResultBean<T> makeResp(int code, String msg) {
        ResultBean<T> resultBean = new ResultBean<T>();
        resultBean.setCode(code);
        resultBean.setMsg(msg);
        return resultBean;
    }

    public static <T> ResultBean<T> makeResp(RespCode code, String msg, T data) {
        ResultBean<T> resultBean = new ResultBean<T>(code);
        resultBean.setMsg(msg);
        resultBean.setData(data);
        return resultBean;
    }

    public static <T> ResultBean<T> makeResp(RespCode code, T data) {
        ResultBean<T> resultBean = new ResultBean<T>(code, data);
        return resultBean;
    }

    public static <T> ResultBean<T> makeResp(Throwable e) {
        return new ResultBean<T>(e);
    }
}
