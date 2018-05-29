package com.yizhigou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.pay.service.WeixinPayService;
import com.yizhigou.pojo.TbSeckillOrder;
import com.yizhigou.seckill.service.SeckillOrderService;
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
    private SeckillOrderService orderService;
    @RequestMapping("/createNative")
    public Map createNative(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        TbSeckillOrder seckillOrder = orderService.searchOrderFromRedisByUserId(userId);
        if (seckillOrder != null) {
            long len = (long) (seckillOrder.getMoney().doubleValue() * 100);
            return weixinPayService.createNative(seckillOrder.getId()+"",len+"");
        }else{
            return new HashMap();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
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
                orderService.saveOrderFromRedisToDb(userId,Long.parseLong(out_trade_no),map.get("transaction_id")+"");
                break;
            }
            try {
                Thread.sleep(3000);
                x++;
                if(x==4){
                    result = new Result(false, "二维码过时");
                    Map closeMap = weixinPayService.closePay(out_trade_no);
                    if(!"SUCCESS".equals(closeMap.get("result_code"))){
                        if ("ORDERPAID".equals(closeMap.get("err_code"))) {
                            result = new Result(true, "支付成功");
                            orderService.saveOrderFromRedisToDb(userId+"",Long.parseLong(out_trade_no),closeMap.get("transaction_id")+"");
                        }
                    }

                    if(result.isSuccess()==false){
                        orderService.deleteOrderFromRedis(userId,Long.parseLong(out_trade_no));
                    }
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
