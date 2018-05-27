package com.yizhigou.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 15:35 2018/5/21
 * @Modified By:
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/showName")
    public String showName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return name;
    }
}
