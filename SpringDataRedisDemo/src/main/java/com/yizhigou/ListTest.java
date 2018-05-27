package com.yizhigou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 20:50 2018/5/10
 * @Modified By:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-redis.xml")
public class ListTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue() {
        /*redisTemplate.boundListOps("name").rightPush("关羽");
        redisTemplate.boundListOps("name").rightPush("张飞");
        redisTemplate.boundListOps("name").rightPush("曹操");*/
        redisTemplate.boundListOps("name").leftPush("刘备");
    }
    @Test
    public void getValue(){
        List name = redisTemplate.boundListOps("name").range(0, 10);
        System.out.println(name);
    }

    @Test
    public void deleteValue(){
        redisTemplate.boundListOps("name").remove(0, "张飞");
        redisTemplate.delete("xi");
    }
}
