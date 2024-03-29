package com.wimetro.cg.config.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.destination.DestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

/**
 * @title: QueueConfig
 * @author: Ellie
 * @date: 2022/04/13 09:29
 * @description:
 **/
@Configuration
public class JmsConfig {
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String userName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.queueName.messageQueue}")
    private String messageQueue;

    @Value("${spring.activemq.queueName.statusQueue}")
    private String statusQueue;


    @Bean("messageQueue")
    public Queue messageQueue() {
        return new ActiveMQQueue(messageQueue);
    }

    @Bean("statusQueue")
    public Queue statusQueue() {
        return new ActiveMQQueue(statusQueue);
    }


    @Bean
    public JmsMessagingTemplate jmsMessageTemplate(ConnectionFactory jmsConnectionFactory){
        return new JmsMessagingTemplate(jmsConnectionFactory);
    }

    /**
     * JMS 队列的监听容器工厂
     */
    @Bean(name = "MyJmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ConnectionFactory jmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(jmsConnectionFactory);

        factory.setConnectionFactory(cachingConnectionFactory);

        factory.setPubSubDomain(false);
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(4);
        factory.setConcurrency("10-20");

        DestinationResolver destinationResolver = (session, destinationName, pubSubDomain) -> {
            Destination destination = session.createQueue(destinationName);
            return destination;
        };

        factory.setDestinationResolver(destinationResolver);
        return factory;
    }

    /**
     * JMS 广播模式监听容器工厂
     */
    @Bean(name = "MyJmsTopicListener")
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory jmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(jmsConnectionFactory);
        factory.setConnectionFactory(cachingConnectionFactory);

        factory.setPubSubDomain(true);
        factory.setSessionTransacted(true);
        factory.setConcurrency("1-3");

        return factory;
    }


    @Bean
    public ConnectionFactory jmsConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setMaxThreadPoolSize(ActiveMQConnection.DEFAULT_THREAD_POOL_SIZE);
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

        //定义ReDelivery(重发机制)机制 ，重发时间间隔是100毫秒，最大重发次数是3次
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为5次
        redeliveryPolicy.setMaximumRedeliveries(6);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(500);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        //最大传送延迟，只在useExponentialBackOff为true时有效（V5.5），假设首次重连间隔为10ms，倍数为2，那么第
        //二次重连时间间隔为 20ms，第三次重连时间间隔为40ms，当重连时间间隔大的最大重连时间间隔时，以后每次重连时间间隔都为最大重连时间间隔。
        redeliveryPolicy.setMaximumRedeliveryDelay(2000);

        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

        return connectionFactory;
    }

}
