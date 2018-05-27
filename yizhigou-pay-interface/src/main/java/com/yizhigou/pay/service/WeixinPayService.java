package com.yizhigou.pay.service;

import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:46 2018/5/24
 * @Modified By:
 **/
public interface WeixinPayService {

    public Map createNative(String out_trade_no, String total_fee);

    public Map queryPayStatus(String out_trade_no);
}
