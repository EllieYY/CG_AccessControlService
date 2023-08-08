package com.wimetro.cg.service;


import com.wimetro.cg.model.mq.DeviceStateMessage;
import com.wimetro.cg.model.mq.MqMessage;
import com.wimetro.cg.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.Queue;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: QueueProducer
 * @author: Ellie
 * @date: 2023/04/12 15:42
 * @description: MQ消息生产者
 **/
@Service
@Slf4j
public class QueueProducer {
    private final JmsMessagingTemplate jmsMessagingTemplate;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Queue messageQueue;
    private final Queue statusQueue;


    @Autowired
    public QueueProducer(JmsMessagingTemplate jmsMessagingTemplate, ThreadPoolTaskExecutor threadPoolTaskExecutor,
                         Queue messageQueue, Queue statusQueue) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.messageQueue = messageQueue;
        this.statusQueue = statusQueue;
    }

    public void sendEventMessage(MqMessage mqMessage) {
        String messageStr = JsonUtil.toJson(mqMessage);
        log.info("[{} - 设备事件] - {}", mqMessage.getSn(), messageStr);
        this.sendDeviceMessage(messageQueue, messageStr);
    }

    public void sendStatusMessage(DeviceStateMessage mqMessage) {
        String messageStr = JsonUtil.toJson(mqMessage);
        log.info("[{} - 设备状态] - {}", mqMessage.getIp(), messageStr);
        this.sendDeviceMessage(statusQueue, messageStr);
    }


    public void sendDeviceMessage(Destination destination, String message) {
        threadPoolTaskExecutor.submit(() -> {
            Date date = new Date();
            try {
                log.info("[mq][queue-->send]:activeCount={},queueCount={},completedTaskCount={},taskCount={}",
                        threadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount(),
                        threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size(),
                        threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount(),
                        threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount());

                this.jmsMessagingTemplate.convertAndSend(destination, message);
            } catch (Throwable e) {
                log.error("{}", e);
            }
        });
    }

    public void sendDelayMessage(Destination destination, String message, long delaySec) {
        threadPoolTaskExecutor.submit(() -> {
            Date date = new Date();
            try {
                Map<String, Object> headers = new HashMap<>();
                // 延迟毫秒
                headers.put(ScheduledMessage.AMQ_SCHEDULED_DELAY, delaySec * 1000);
                this.jmsMessagingTemplate.convertAndSend(destination, message, headers);
            } catch (Throwable e) {
                log.error("{}", e);
            }
        });
    }

    public void sendMapMessage(String queueName, Object message) {
        threadPoolTaskExecutor.submit(() -> {
            try {
                Destination destination = new ActiveMQQueue(queueName);
                // 这里定义了Queue的key
                ActiveMQMapMessage mqMapMessage = new ActiveMQMapMessage();
                mqMapMessage.setJMSDestination(destination);
                mqMapMessage.setObject("result", message);
                this.jmsMessagingTemplate.convertAndSend(destination, mqMapMessage);
            } catch (Throwable e) {
                log.error("{}", e);
            }
        });
    }

    public void sendObjectMessage(String queueName, Object message) {
        threadPoolTaskExecutor.submit(() -> {
            try {
                log.info("sendObjectMessage:{}",message.toString());
                Destination destination = new ActiveMQQueue(queueName);
                ActiveMQObjectMessage mqObjectMessage = new ActiveMQObjectMessage();
                mqObjectMessage.setJMSDestination(destination);
                mqObjectMessage.setObject((Serializable) message);
                this.jmsMessagingTemplate.convertAndSend(destination, mqObjectMessage);
            } catch (Throwable e) {
                log.error("{}", e);
            }
        });
    }



}
