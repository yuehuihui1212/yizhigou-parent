package com.yizhigou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 14:43 2018/5/13
 * @Modified By:
 **/
@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> search(Map searchMap) {
        Map map = new HashMap();
        String keywords = (String) searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            map.put("keywords", keywords.replace(" ", ""));
        }
        map.putAll(searchList(searchMap));
        List categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);
        String categoryName = (String) searchMap.get("category");
        if(!"".equals(categoryName)){
            map.putAll(searchBrandAndspecList(categoryName));
        }else{
            if(categoryList.size()>0){
                map.putAll(searchBrandAndspecList(categoryList.get(0)+""));
            }
        }
        return map;
    }

    private Map searchList(Map searchMap){
        Map map = new HashMap();
        HighlightQuery highlightQuery = new SimpleHighlightQuery();
        //按照类别查询
        if(!"".equals(searchMap.get("category"))){
            Criteria categoryCriteria = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            highlightQuery.addFilterQuery(filterQuery);
        }
        //按照品牌查询
        if(!"".equals(searchMap.get("brand"))){
            Criteria brandCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(brandCriteria);
            highlightQuery.addFilterQuery(filterQuery);
        }
        //按照规格和规格项来查询
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map) searchMap.get("spec");
            for(String key:specMap.keySet()){
                Criteria criteria = new Criteria("item_spec_" + key).is(searchMap.get(key));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria);
                highlightQuery.addFilterQuery(filterQuery);
            }
        }
        //按照价格查询
        if(!"".equals(searchMap.get("price"))){
            String[] price= ((String) searchMap.get("price")).split("-");
            if(!price[0].equals("0")){
                Criteria criteria = new Criteria("item_price").greaterThanEqual(price[0]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria);
                highlightQuery.addFilterQuery(filterQuery);
            }
            if (!price[1].equals("*")) {
                    Criteria criteria = new Criteria("item_price").lessThanEqual(price[1]);
                    SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria);
                    highlightQuery.addFilterQuery(filterQuery);
            }
        }

        //分页
        Integer  pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null) {
            pageNo=1;
        }
        Integer pageSize= (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize=40;
        }
        String sortValue = (String) searchMap.get("sort");
        String sortFeild = (String) searchMap.get("sortField");
        if(sortValue!=null && !sortFeild.equals("")){
            if(sortValue.equals("ASC")){
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortFeild);
                highlightQuery.addSort(sort);
            }
        }
        if(sortValue!=null && !sortFeild.equals("")){
            if(sortValue.equals("DESC")){
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortFeild);
                highlightQuery.addSort(sort);
            }
        }
        highlightQuery.setOffset((pageNo - 1) * pageSize);
        highlightQuery.setRows(pageSize);
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        highlightQuery.setHighlightOptions(highlightOptions);
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        highlightQuery.addCriteria(criteria);
        HighlightPage<TbItem> tbItems = solrTemplate.queryForHighlightPage(highlightQuery, TbItem.class);
        for(HighlightEntry<TbItem> h:tbItems.getHighlighted()){
            TbItem item = h.getEntity();
            if(h.getHighlights().size()>0 && h.getHighlights().get(0).getSnipplets().size()>0){
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }
        map.put("rows",tbItems.getContent());
        map.put("totalPages", tbItems.getTotalPages());
        map.put("total", tbItems.getTotalElements());
        return map;
    }
    private List searchCategoryList(Map searchMap){
        List list = new ArrayList<>();
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions category = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(category);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> item_category = page.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = item_category.getGroupEntries();
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for(GroupEntry<TbItem> item:content){
            list.add(item.getGroupValue());
        }
        return list;
    }

    private Map searchBrandAndspecList(String category){
        HashMap map = new HashMap<>();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if(typeId!=null){
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }
        return map;
    }

    //向索引库导入句
    @Override
    public void importData(List<TbItem> list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsId(List goodsIdList) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
        System.out.println("兄弟我已经删除完索引库中的商品了===========");
    }
}
