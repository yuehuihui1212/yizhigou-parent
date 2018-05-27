package com.yizhigou;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 20:50 2018/5/10
 * @Modified By:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-redis.xml")
public class SetTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue() {
        redisTemplate.boundSetOps("xi").add("孙悟空");
        redisTemplate.boundSetOps("xi").add("猪八戒");
        redisTemplate.boundSetOps("xi").add("沙和尚");
    }
    @Test
    public void getValue(){
        Set xi = redisTemplate.boundSetOps("xi").members();
        System.out.println(xi);
    }

    @Test
    public void deleteValue(){
        //redisTemplate.boundSetOps("xi").remove("孙悟空");
        redisTemplate.delete("xi");
    }
}
