package com.wimetro.cg.config.aop;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @Description : 将rest template内部的异常处理抛出
 * @Author : Ellie
 * @Date : 2019/3/5
 */
public class RestTemplateThrowErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    }

}
