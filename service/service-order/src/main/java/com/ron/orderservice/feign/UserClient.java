package com.ron.orderservice.feign;

import com.ron.commonutils.R;
import com.ron.commonutils.orderVo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient("service-ucenter")
public interface UserClient {
    @GetMapping("/educenter/ucenter-member/getUserByid/{id}")
    public UcenterMemberOrder getUserByid(@PathVariable("id")String id);
}
