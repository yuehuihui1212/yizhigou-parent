package com.yizhigou.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 14:48 2018/5/17
 * @Modified By:
 **/
public class TopicMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message){
        TextMessage textMessage= (TextMessage) message;
        try {
            System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
