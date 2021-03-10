package com.ron.orderservice.controller;


import com.ron.commonutils.R;
import com.ron.orderservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
@RestController
@CrossOrigin
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成二维码
    @GetMapping("/createNative/{orderNo}")
    public R cretaeNative(@PathVariable("orderNo") String orderNo){
        Map map=payLogService.createNative(orderNo);

        System.out.println("二维码信息+::::::"+map);
        //返回信息,包含二维码地址和其他信息
        return R.ok().data(map);
    }

    //查询二维码支付状态
    @GetMapping("/queryStatus/{orderNo}")
    public R queryStatus(@PathVariable("orderNo") String orderNo){
        Map<String,String> map=payLogService.queryStatus(orderNo);//根据订单号查询二维码支付状态
        System.out.println("二维码返回结果:@@@"+map);
        if (map==null){//支付失败
            return R.error().message("支付失败");
        }

        if (map.get("trade_state").equals("SUCCESS")){//微信二维码返回的map中trade_state为Success则支付成功
            //添加记录到支付记录,修改订单状态
            payLogService.updateOrderStatus(map);
            //返回信息
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");


    }
}

