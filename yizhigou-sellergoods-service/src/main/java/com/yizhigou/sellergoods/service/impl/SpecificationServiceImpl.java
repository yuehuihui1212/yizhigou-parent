package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.mapper.TbSpecificationMapper;
import com.yizhigou.mapper.TbSpecificationOptionMapper;
import com.yizhigou.pojo.TbSpecification;
import com.yizhigou.pojo.TbSpecificationExample;
import com.yizhigou.pojo.TbSpecificationOption;
import com.yizhigou.pojo.TbSpecificationOptionExample;
import com.yizhigou.pojogroup.Specification;
import com.yizhigou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		specificationMapper.insert(specification.getSpecification());
		for(TbSpecificationOption specificationOption : specification.getSpecificationOptionList()){
			specificationOption.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(specificationOption);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
		for(TbSpecificationOption specificationOption:specification.getSpecificationOptionList()){
		    specificationOptionMapper.updateByPrimaryKey(specificationOption);
        }
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
	    //根据ID查询规格
        TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
        //创建实例来设置查询条件
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //添加规格ID用于查询规格项
        criteria.andSpecIdEqualTo(id);
        //根据ID查询出该规格下的所有规格项
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
        Specification specification1 = new Specification();
        specification1.setSpecification(specification);
        specification1.setSpecificationOptionList(specificationOptionList);
        return specification1;
    }

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
        for(Long id:ids){
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
			specificationMapper.deleteByPrimaryKey(id);

		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		TbSpecificationExample.Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return specificationMapper.selectOptionList();
	}

}
