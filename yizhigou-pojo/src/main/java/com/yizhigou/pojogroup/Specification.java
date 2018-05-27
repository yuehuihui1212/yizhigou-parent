package com.yizhigou.pojogroup;

import com.yizhigou.pojo.TbSpecification;
import com.yizhigou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 17:26 2018/5/3
 * @Modified By:
 **/
public class Specification implements Serializable {
    private TbSpecification specification;
    private List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
