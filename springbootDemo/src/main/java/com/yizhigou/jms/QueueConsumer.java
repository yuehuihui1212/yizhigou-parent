package com.yizhigou.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 11:13 2018/5/18
 * @Modified By:
 **/
@Component
public class QueueConsumer {

    @JmsListener(destination = "jd_test")
    public void readMessage(String text) {
        System.out.println("我已经收到消息了==============="+text);
    }
    @JmsListener(destination = "jd_map_test")
    public void readMapMessage(Map hashMap) {
        System.out.println(hashMap.toString());
    }
}
