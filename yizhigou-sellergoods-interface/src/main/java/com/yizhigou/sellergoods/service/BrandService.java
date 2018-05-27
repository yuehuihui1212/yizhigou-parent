package com.yizhigou.sellergoods.service;

import com.yizhigou.pojo.TbBrand;
import entity.Result;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 21:22 2018/4/30
 * @Modified By:
 **/
public interface BrandService {
    public List<TbBrand> getList();

    //分页查询
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    //添加
    public Result save(TbBrand brand);

    //根据ID查询
    public TbBrand findOne(Long id);

    //修改
    public Result update(TbBrand brand);

    //删除
    public Result delete(Long[] ids);

    List<Map> selectOptionList();

}
