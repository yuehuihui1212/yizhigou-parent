package com.yizhigou.pojogroup;

import com.yizhigou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:52 2018/5/22
 * @Modified By:
 **/
public class Cart implements Serializable {
    private String sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    private String sellerName;



    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    private List<TbOrderItem> orderItemList;


}
