package com.yizhigou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.order.service.OrderService;
import com.yizhigou.pay.service.WeixinPayService;
import com.yizhigou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 17:12 2018/5/24
 * @Modified By:
 **/
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeixinPayService weixinPayService;
    @Reference
    private OrderService orderService;
    @RequestMapping("/createNative")
    public Map createNative(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        if (payLog != null) {

            return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");
        }else{
            return new HashMap();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result=null;
        int x=0;
        while (true) {
            Map map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "支付失败");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {
                result = new Result(true, "支付成功");
                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id")+"");
                break;
            }
            try {
                Thread.sleep(3000);
                x++;
                if(x>=100){
                    result = new Result(false, "二维码过时");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
