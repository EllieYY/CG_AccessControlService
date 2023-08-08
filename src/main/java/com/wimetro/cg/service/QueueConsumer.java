package com.wimetro.cg.service;

import com.wimetro.cg.netty.runner.RequestPendingCenter;
import com.wimetro.cg.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @title: QueueConsumer
 * @author: Ellie
 * @date: 2022/04/13 16:39
 * @description:
 **/
@Service
@Configuration
@Slf4j
public class QueueConsumer {
    private final RequestPendingCenter requestPendingCenter;

    @Autowired
    public QueueConsumer(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }


//    @JmsListener(destination = "cgcgQueue", containerFactory = "MyJmsQueueListener")
//    public void receiveCgMsg(Message message, Session session) throws JMSException {
//        TextMessage textMessage = (TextMessage)message;
//        log.info("{}", textMessage);
//    }
}
