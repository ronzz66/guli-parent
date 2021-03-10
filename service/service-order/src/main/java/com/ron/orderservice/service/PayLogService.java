package com.ron.orderservice.service;

import com.ron.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    Map<String,String> queryStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
