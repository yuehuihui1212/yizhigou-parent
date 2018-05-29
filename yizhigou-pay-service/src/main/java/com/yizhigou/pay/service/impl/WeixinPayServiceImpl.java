package com.yizhigou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.yizhigou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import utils.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:47 2018/5/24
 * @Modified By:
 **/
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        Map<String,String> map=new HashMap<>();
        map.put("appid", appid);//公众账号ID
        map.put("mch_id", partner);//商户号
        map.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符窜
        map.put("body", "易直购");//商品描述
        map.put("out_trade_no", out_trade_no);//订单编号
        map.put("total_fee", total_fee);//订单总金额，单位为分
        map.put("spbill_create_ip", "127.0.0.1");//调用微信支付API的服务器IP
        map.put("notify_url", "http://www.yizhigou.com");//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        map.put("trade_type", "NATIVE");//支付方式 NATIVE
        try {
            String signedXml = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("发送的参数===="+signedXml);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(signedXml);
            client.post();

            //获取返回结果
            String content = client.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("code_url", xmlToMap.get("code_url"));//支付地址
            stringMap.put("out_trade_no", out_trade_no);//订单号
            stringMap.put("total_fee", total_fee);//总金额
            System.out.println(stringMap);
            return stringMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        Map<String, String> param = new HashMap<>();
        param.put("appid", appid);//公众号ID
        param.put("mch_id",partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符窜
        param.put("out_trade_no", out_trade_no);
        try {
            String mapToXml = WXPayUtil.generateSignedXml(param,partnerkey);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setHttps(true);
            client.setXmlParam(mapToXml);
            client.post();

            //获取返回结果
            String content = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map closePay(String out_trade_no) {
        Map<String, String> param = new HashMap<>();
        param.put("appid", appid);//公众号ID
        param.put("mch_id",partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符窜
        param.put("out_trade_no", out_trade_no);
        try {
            String mapToXml = WXPayUtil.generateSignedXml(param,partnerkey);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            client.setHttps(true);
            client.setXmlParam(mapToXml);
            client.post();

            //获取返回结果
            String content = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
