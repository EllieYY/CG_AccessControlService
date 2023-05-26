package com.wimetro.cg.config.aop.excption;

/**
 * @Description : 数据库操作异常
 * @Author : Ellie
 * @Date : 2019/3/4
 */
public class DataDaoException extends RuntimeException {
    public DataDaoException(String msg) {
        super(msg);
    }
}
