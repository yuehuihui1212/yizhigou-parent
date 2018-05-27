package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.mapper.TbBrandMapper;
import com.yizhigou.pojo.TbBrand;
import com.yizhigou.pojo.TbBrandExample;
import com.yizhigou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 21:23 2018/4/30
 * @Modified By:
 **/
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> getList() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        TbBrandExample tbBrandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        if(brand!=null){
            if(brand.getName()!=null && brand.getName()!=""){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(brand.getFirstChar()!=null && brand.getFirstChar()!=""){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<TbBrand> page= (Page<TbBrand>) brandMapper.selectByExample(tbBrandExample);
      return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Result save(TbBrand brand) {
        try {
            brandMapper.insert(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    @Override
    public TbBrand findOne(Long id) {
       return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public Result update(TbBrand brand) {
        try {
            brandMapper.updateByPrimaryKey(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }

    @Override
    public Result delete(Long[] ids) {
        try {
            for (Long id :ids) {
                brandMapper.deleteByPrimaryKey(id);
            }
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }

    public List<Map> selectOptionList(){
        return brandMapper.selectOptionList();
    }
}
