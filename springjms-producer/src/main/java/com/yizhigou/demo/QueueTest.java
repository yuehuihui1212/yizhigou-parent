package com.yizhigou.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 13:55 2018/5/17
 * @Modified By:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-jms-producer.xml")
public class QueueTest {
    @Autowired
    private QueueProducer queueProducer;
    @Autowired
    private TopicProducer topicProducer;

    @Test
    public void test() {
        queueProducer.sendMessage("你好啊，ActiveMQ");
    }

    @Test
    public void topicTest(){
        topicProducer.sendMessage("你好，ActiveMQ,我是通过Topic过来的");
    }
}
