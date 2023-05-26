package com.wimetro.cg.netty.handler.dispatcher;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

/**
 * @title: MyListener
 * @author: Ellie
 * @date: 2023/03/29 16:10
 * @description:
 **/
public class MyListener implements GenericFutureListener {
    private Promise promise;
    MyListener(Promise promise) {
        this.promise = promise;
    }

    @Override
    public void operationComplete(Future future) throws Exception {
        System.out.println("回调成功");
        promise.setSuccess(null);
    }
}
