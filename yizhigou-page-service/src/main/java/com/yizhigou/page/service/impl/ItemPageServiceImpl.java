package com.yizhigou.page.service.impl;

import com.yizhigou.mapper.TbGoodsDescMapper;
import com.yizhigou.mapper.TbGoodsMapper;
import com.yizhigou.mapper.TbItemCatMapper;
import com.yizhigou.mapper.TbItemMapper;
import com.yizhigou.page.service.ItemPageService;
import com.yizhigou.pojo.TbGoods;
import com.yizhigou.pojo.TbGoodsDesc;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 21:22 2018/5/16
 * @Modified By:
 **/
@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Value("${pagedir}")
    private String pagedir;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public boolean genItemHtml(Long goodsId){
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map dataModel = new HashMap<>();
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goods", goods);
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goodsDesc", goodsDesc);
            String category1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            dataModel.put("category1", category1);
            String category2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            dataModel.put("category2", category2);
            String category3= itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataModel.put("category3", category3);

            TbItemExample example=new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andGoodsIdEqualTo(goodsId);
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList", itemList);
            FileWriter out = new FileWriter(new File(pagedir + goodsId + ".html"));
            template.process(dataModel,out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteItemHtml(Long[] goodsId) {
        try {
            for(Long id:goodsId){
                new File(pagedir + id + ".html").delete();

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
