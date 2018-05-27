package com.yizhigou.search.service.impl;

import com.yizhigou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 17:11 2018/5/17
 * @Modified By:
 **/
@Component
public class ItemDeleteListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage= (ObjectMessage) message;
            Long [] ids= (Long[]) objectMessage.getObject();
            System.out.println("ActiveMQ 接收到需要删除的ids=================");
            itemSearchService.deleteByGoodsId(Arrays.asList(ids));
            System.out.println("ActiveMQ 删除索引库成功=======================");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
