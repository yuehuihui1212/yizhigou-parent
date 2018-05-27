package com.yizhigou.page.service.impl;

import com.yizhigou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 17:37 2018/5/17
 * @Modified By:
 **/
public class PageDeleteMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage= (ObjectMessage) message;
            Long[] ids= (Long[]) objectMessage.getObject();
            System.out.println("接收到ActiveMQ的删除静态页面的goodsId");
            itemPageService.deleteItemHtml(ids);
            System.out.println("静态页面已经成功删除了=============");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
