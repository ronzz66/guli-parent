package com.ron.orderservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.commonutils.JwtUtils;
import com.ron.commonutils.R;
import com.ron.orderservice.entity.Order;
import com.ron.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/orderservice/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;


    //生成订单
    @PostMapping("/createOrder/{courseId}")//生成订单
    public R createOrder(@PathVariable("courseId")String courseId,HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);//从request的header中的token获取用户id
        //创建订单
        String orderNum=orderService.cretaeOrder(courseId,id);//生成订单号


        return R.ok().data("orderId",orderNum);
    }

    //根据订单id查询订单信息
    @GetMapping("/getOrder/{orderId}")
    public R getOrderById(@PathVariable("orderId")String orderId){


        //根据订单id查询订单
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);


        return R.ok().data("item",order);
    }

    //根据课程id和用户id查询课程订单是否被购买
    @GetMapping("/getOrderStatus/{courseId}/{memberId}")
    public boolean getOrderStatus(@PathVariable("courseId")String courseId,
                            @PathVariable("memberId")String memberId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status","1");//1为已经购买

        int count = orderService.count(wrapper);
        if (count>0){
            return true;
        }


        return false;
    }


}

