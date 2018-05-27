package com.yizhigou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.yizhigou.cart.service.CartService;
import com.yizhigou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 20:36 2018/5/22
 * @Modified By:
 **/
@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        String cartList = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartList == null || cartList.equals("")) {
            cartList = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartList, Cart.class);
        if(name.equals("anonymousUser")){
           return cartList_cookie;
        }else{
            List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
            if (cartList_cookie.size() > 0) {
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                CookieUtil.deleteCookie(request,response,"cartList");
                cartService.addGoodsToRedis(name,cartList_redis);
            }
            return cartList_redis;
        }

    }

    @RequestMapping("addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9105")
    public Result addGoodsToCartList(Long itemId, int num) {
        try {
            List<Cart> cartList = findCartList();
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if(name.equals("anonymousUser")){
                System.out.println("向cookie中存储=================");
                CookieUtil.setCookie(request,response,"cartList",JSON.toJSONString(cartList),3600*24,"UTF-8");
            }else{
                System.out.println("合并Controller==================");
                cartService.addGoodsToRedis(name,cartList);
            }
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }
}
