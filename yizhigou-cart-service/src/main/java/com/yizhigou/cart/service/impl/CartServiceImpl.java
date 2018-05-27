package com.yizhigou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yizhigou.cart.service.CartService;
import com.yizhigou.mapper.TbItemMapper;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.pojo.TbOrderItem;
import com.yizhigou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:56 2018/5/22
 * @Modified By:
 **/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, int num) {
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("商品状态不正确");
        }
        //1.根据商品SKU ID查询SKU商品信息
        String sellerId = item.getSellerId();
        //2.获取商家ID
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart = searchCartBySellerId(cartList, sellerId);
        //4.如果购物车列表中不存在该商家的购物车
        if(cart==null){
            //4.1 新建购物车对象
            cart=new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            TbOrderItem orderItem = createOrderItem(item, num);
            List<TbOrderItem> orderItemList=new ArrayList<>();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4.2 将新建的购物车对象添加到购物车列表
            cartList.add(cart);
        }else{

            //5.如果购物车列表中存在该商家的购物车
            // 查询购物车明细列表中是否存在该商品
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), item);
            if (orderItem == null) {
                cart.getOrderItemList().add(createOrderItem(item, num));
            }else{
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(item.getPrice().longValue()*orderItem.getNum()));
                if(orderItem.getNum()<=0){
                    cart.getOrderItemList().remove(orderItem);
                }
                if (cart.getOrderItemList().size() <= 0) {
                    cartList.remove(cart);
                }
            }
            //5.1. 如果没有，新增购物车明细
            //5.2. 如果有，在原购物车明细上添加数量，更改金额
        }
        return cartList;
    }


    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if(cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    private TbOrderItem createOrderItem(TbItem item, int num) {
        if(num<=0){
            throw new RuntimeException("非法数量");
        }
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().longValue()*num));
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        return orderItem;
    }

    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, TbItem item) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == item.getId().longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    @Override
    public void addGoodsToRedis(String name, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(name,cartList);
    }

    @Override
    public List<Cart> findCartListFromRedis(String name) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(name);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        System.out.println("合并购物车开始=================");
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1=addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }
        System.out.println("合并购物车结束=================");
        return cartList1;
    }
}
