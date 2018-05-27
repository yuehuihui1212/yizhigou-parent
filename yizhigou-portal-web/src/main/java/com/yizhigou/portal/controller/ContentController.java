package com.yizhigou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.content.service.ContentService;
import com.yizhigou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 19:58 2018/5/10
 * @Modified By:
 **/
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;
    
    /**
      *@Author: yuehuihui
      *@Description:
      *@Date: 19:59 2018/5/10
    */
    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }
}
