package com.yizhigou.com.yizhigou.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 10:23 2018/5/18
 * @Modified By:
 **/
@RestController
public class SpringBootDemo {

    @RequestMapping("/demo")
    public String firstDemo(String text) {
        return text + "欢迎来到springBoot";
    }
}
