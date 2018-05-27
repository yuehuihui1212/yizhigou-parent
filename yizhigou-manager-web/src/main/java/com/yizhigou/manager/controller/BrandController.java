package com.yizhigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.pojo.TbBrand;
import com.yizhigou.sellergoods.service.BrandService;
import entity.Result;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 21:24 2018/4/30
 * @Modified By:
 **/
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.getList();
    }

    //分页查询
    @RequestMapping("/search")
    public PageResult findPage(@RequestBody TbBrand brand, int page, int rows){
        return brandService.findPage(brand,page,rows);
    }

    //添加频偏
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand){
        return brandService.save(brand);
    }
    //根据id查询
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }
    //修改
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand){
        return brandService.update(brand);
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        return brandService.delete(ids);
    }

    //用于下拉框架添加品牌
    @RequestMapping("/selectOptionList")
    public  List<Map> selectOptionList(){
        return brandService.selectOptionList();
    }
}
