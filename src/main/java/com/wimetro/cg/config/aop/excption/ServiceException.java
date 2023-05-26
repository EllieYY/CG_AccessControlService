package com.wimetro.cg.config.aop.excption;

/**
 * @Description : 服务端异常
 * @Author : Ellie
 * @Date : 2019/3/4
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }
}
