package com.yizhigou.pojogroup;

import com.yizhigou.pojo.TbGoods;
import com.yizhigou.pojo.TbGoodsDesc;
import com.yizhigou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 19:45 2018/5/7
 * @Modified By:
 **/
public class Goods implements Serializable {
    private TbGoods goods;
    private TbGoodsDesc goodsDesc;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    private List<TbItem> itemList;


}
