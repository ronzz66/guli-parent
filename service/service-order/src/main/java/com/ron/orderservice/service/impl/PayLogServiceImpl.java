package com.ron.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import com.ron.exceptionhandler.GuliException;
import com.ron.orderservice.entity.Order;
import com.ron.orderservice.entity.PayLog;
import com.ron.orderservice.mapper.PayLogMapper;
import com.ron.orderservice.service.OrderService;
import com.ron.orderservice.service.PayLogService;
import com.ron.orderservice.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static sun.security.krb5.Confounder.longValue;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;//查询订单信息

    //生成微信二维码
    @Override
    public Map createNative(String orderNo) {
        try {
            //1.根据订单id查询订单信息
            QueryWrapper<Order> wrapper=new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);
            //2.使用map设置生成二维码需要的参数
            Map map=new HashMap();
            map.put("appid","wx74862e0dfcf69954");//微信appid
            map.put("mch_id", "1558950191");//商户号
            map.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串让二维码不一样
            map.put("body", order.getCourseTitle());//二维码信息,这里是订单中的课程名称
            map.put("out_trade_no", orderNo);//二维码唯一标识 这里是订单号

            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//价格
            map.put("spbill_create_ip", "127.0.0.1");//项目域名
            map.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");//支付后回调
            map.put("trade_type", "NATIVE");//支付类型,
            //3.发送httpclient请求,传递xml格式,到微信支付提供的固定地址
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
                //设置xml格式的参数,传入商户key
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(map,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true);//支持https请求
            httpClient.post();//发送
            //4.得到返回结果
            String content = httpClient.getContent();//获取返回内容,格式为xml内容
            //转换成map
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);

            //封装成前台需要的map
            Map resultMap = new HashMap<>();
            resultMap.put("out_trade_no", orderNo);//订单号
            resultMap.put("course_id", order.getCourseId());
            resultMap.put("total_fee", order.getTotalFee());
            resultMap.put("result_code", xmlToMap.get("result_code"));//生成二维码操作的状态码
            resultMap.put("code_url", xmlToMap.get("code_url"));//二维码地址
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new GuliException(20001,"生成二维码失败");
        }
    }

    //查询二维码支付状态
    @Override
    public Map<String, String> queryStatus(String orderNo) {
        try {
            //封装参数
            Map map=new HashMap();
            map.put("appid","wx74862e0dfcf69954");//微信的appid
            map.put("mch_id", "1558950191");//商户号
            map.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串让二维码不一样
            map.put("out_trade_no", orderNo);//二维码唯一标识 这里是订单号
            //发送httpclient请求
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");//查询二维码的支付状态
                //设置xml格式的参数,传入商户key
            client.setXmlParam(WXPayUtil.generateSignedXml(map,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));

            client.setHttps(true);
            client.post();//发送请求
            String content = client.getContent();//获取返回内容,格式为xml


            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);//返回结果xml转为map

            return xmlToMap;
        } catch (Exception e) {
            return null;
        }

    }

    //支付成功 添加支付记录
    //支付成功 更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        String orderNo = map.get("out_trade_no");//获取订单号
        //1.根据订单id查询订单信息
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        if (order.getStatus()==1){//查看订单状态如果==1则已经支付,不需要修改,直接返回
            return;
        }
        order.setStatus(1);
        orderService.updateById(order);//更新订单状态
        //向支付日志表添加记录
        PayLog payLog =new PayLog();
        payLog.setOrderNo(orderNo);//订单号
        payLog.setPayType(1);//支付类型
        payLog.setPayTime(new Date());//支付时间
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态,请求微信支付状态查询返回
        payLog.setTransactionId(map.get("transaction_id"));//订单流水号.请求微信支付状态查询返回
        payLog.setAttr(JSONObject.toJSONString(map));//其他属性
        baseMapper.insert(payLog);


    }
}
