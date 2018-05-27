package com.yizhigou;

import com.yizhigou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 10:39 2018/5/13
 * @Modified By:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-solr.xml")
public class SolrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void addSolr(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setGoodsId(1L);
        tbItem.setBrand("OPPO");
        tbItem.setPrice(new BigDecimal(1000));
        tbItem.setTitle("这是一个手机");
        tbItem.setUpdateTime(new Date());
        tbItem.setCategory("手机");
        tbItem.setImage("192.168.177.139:group/MM0....");
        tbItem.setSeller("华为官方旗舰店");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();

    }
    @Test
    public void get(){
        TbItem byId = (TbItem)solrTemplate.getById(1, TbItem.class);
        System.out.println(byId.getTitle());
    }
    @Test
    public void delete(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void addList(){
        List<TbItem> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            TbItem tbItem = new TbItem();
            tbItem.setId(1L+i);
            tbItem.setGoodsId(1L+i);
            tbItem.setBrand("OPPO");
            tbItem.setPrice(new BigDecimal(1000+100));
            tbItem.setTitle("这是一个手机");
            tbItem.setUpdateTime(new Date());
            tbItem.setCategory("手机");
            tbItem.setImage("192.168.177.139:group/MM0....");
            tbItem.setSeller("华为官方旗舰店");
            list.add(tbItem);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }
    @Test
    public void queryPageList(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(10);
        query.setRows(20);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        long totalElements = page.getTotalElements();
        System.out.println(totalElements);
        List<TbItem> content = page.getContent();
        showList(content);

    }
    //条件查询
    @Test
    public void queryList(){
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_goodsid").endsWith("2");
        //criteria=criteria.and("item_goodsid").endsWith("1");
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        long totalElements = page.getTotalElements();
        System.out.println(totalElements);
        List<TbItem> content = page.getContent();
        showList(content);

    }

    @Test
    public void deleteAll(){
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
    public void showList(List<TbItem> list){
        for (TbItem item:list){
            System.out.println(item.getTitle()+"=============="+item.getGoodsId());
        }
    }
}
