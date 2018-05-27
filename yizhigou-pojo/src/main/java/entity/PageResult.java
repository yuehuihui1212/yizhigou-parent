package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 12:40 2018/5/2
 * @Modified By:
 **/
public class PageResult implements Serializable {

    private Long total;
    private List rows;


    public PageResult() {
    }

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
