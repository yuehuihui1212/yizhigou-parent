package com.yizhigou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 15:50 2018/5/17
 * @Modified By:
 **/
@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private Destination queueSolrDestination;
    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message){
        try {
            TextMessage textMessage= (TextMessage) message;
            List<TbItem> itemList = JSON.parseArray(textMessage.getText(), TbItem.class);
            for (TbItem item : itemList) {
                Map json = JSON.parseObject(item.getSpec());
                item.setSpecMap(json);
            }
            itemSearchService.importData(itemList);
            System.out.println("恭喜你已经成功导入索引库了=======================");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
