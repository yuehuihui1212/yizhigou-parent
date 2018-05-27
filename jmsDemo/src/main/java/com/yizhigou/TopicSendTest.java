package com.yizhigou;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 11:16 2018/5/17
 * @Modified By:
 **/
public class TopicSendTest {
    public static void main(String[] args)throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.177.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(topic);
        TextMessage textMessage = session.createTextMessage("欢迎来到ActiveMQ，topic带你飞");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }
}
