package com.ron.orderservice.service;

import com.ron.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
public interface OrderService extends IService<Order> {

    String cretaeOrder(String courseId, String userId);
}
