package com.yizhigou.cart.service;

import com.yizhigou.pojogroup.Cart;

import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:54 2018/5/22
 * @Modified By:
 **/
public interface CartService {

    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, int num);


    public void addGoodsToRedis(String name, List<Cart> cartList);

    public List<Cart> findCartListFromRedis(String name);

    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);
}
