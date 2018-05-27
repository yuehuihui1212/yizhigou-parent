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
public class HashTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue() {
       redisTemplate.boundHashOps("nv").put("1","宋江");
       redisTemplate.boundHashOps("nv").put("2","吴用");
       redisTemplate.boundHashOps("nv").put("3","武松");
    }
    @Test
    public void getValue(){
        String nv = (String) redisTemplate.boundHashOps("nv").get("3");
        System.out.println(nv);
    }

    @Test
    public void deleteValue(){
       redisTemplate.boundHashOps("nv").delete("1");
      //  redisTemplate.delete("xi");
    }
}
