package com.yizhigou.search.service;

import com.yizhigou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 14:36 2018/5/13
 * @Modified By:
 **/
public interface ItemSearchService {

    public Map<String, Object> search(Map searchMap);

    //向索引库导入数据
    public void importData(List<TbItem> list);

    //根据goodsID删除索引库
    public void deleteByGoodsId(List goodsIdList);
}
