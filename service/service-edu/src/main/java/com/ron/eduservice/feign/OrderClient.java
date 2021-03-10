package com.ron.eduservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderClient {
    //根据课程id和用户id查询课程订单是否被购买
    @GetMapping("/orderservice/order/getOrderStatus/{courseId}/{memberId}")
    public boolean getOrderStatus(@PathVariable("courseId")String courseId,
                                  @PathVariable("memberId")String memberId);
}
