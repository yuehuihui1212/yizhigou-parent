package com.yizhigou.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 11:09 2018/5/18
 * @Modified By:
 **/
@RestController
public class QueueProvider {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @RequestMapping("/send")
    public void send(String text) {
        jmsMessagingTemplate.convertAndSend("jd_test",text);
    }
    @RequestMapping("/sendMap")
    public void sendMap(){
        Map hashMap = new HashMap<>();
        hashMap.put("mobile", "16619765933");
        hashMap.put("template_code", "SMS_135042020");
        hashMap.put("sign_name", "勿忘初心");
        hashMap.put("param", "{\"code\":\"1212\"}");
        jmsMessagingTemplate.convertAndSend("sms",hashMap);
    }
}
