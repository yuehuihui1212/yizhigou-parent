package com.yizhigou.page.service;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 21:13 2018/5/16
 * @Modified By:
 **/
public interface ItemPageService {
    public boolean genItemHtml(Long goodsId);

    public boolean deleteItemHtml(Long[] goodsId);
}
