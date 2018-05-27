package com.yizhigou.page.service.impl;

import com.yizhigou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 17:26 2018/5/17
 * @Modified By:
 **/
@Component
public class PageMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage= (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("ActiveMQ 已经接收到要生成详情页面的goodsId了");
            itemPageService.genItemHtml((Long.parseLong(text)));
            System.out.println("已经成功生成详情页面了");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
