package com.yizhigou.solrutil;

import com.alibaba.fastjson.JSON;
import com.yizhigou.mapper.TbItemMapper;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 11:59 2018/5/13
 * @Modified By:
 **/
@Component
public class SolrUtil {
    @Autowired
    private  TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    //导入数据
    public void importItemData(){
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
        System.out.println("======商品导入开始======");
        for (TbItem item:tbItems){
            Map map = JSON.parseObject(item.getSpec());
            item.setSpecMap(map);
            System.out.println(item.getTitle());
        }
        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();
        System.out.println("======商品导入结束======");
    }
    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        System.out.println("jdkfjdkfjkdfj");
        SolrUtil solrUtil = (SolrUtil) applicationContext.getBean("solrUtil");
        solrUtil.importItemData();
    }
}
