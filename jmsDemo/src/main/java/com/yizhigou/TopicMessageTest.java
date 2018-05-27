package com.yizhigou;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 11:46 2018/5/17
 * @Modified By:
 **/
public class TopicMessageTest {
    public static void main(String[] args) throws  Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.177.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage= (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    /*System.in.read();
    consumer.close();
    session.close();
    connection.close();*/
    }
}
