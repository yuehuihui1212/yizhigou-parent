package com.yizhigou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 20:50 2018/5/10
 * @Modified By:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-redis.xml")
public class StringTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue() {
        redisTemplate.boundValueOps("yue").set("hui");
    }
    @Test
    public void getValue(){
        String name =(String) redisTemplate.boundValueOps("yue").get();
        System.out.println(name);
    }

    @Test
    public void deleteValue(){
        redisTemplate.delete("yue");
    }
}
