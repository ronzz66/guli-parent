package com.ron.orderservice.feign;


import com.ron.commonutils.orderVo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface CourseClient {
    //根据课程的id查询课程的信息
    @PostMapping("/eduservice/courseFront/getCourseInfo/{id}")
    public CourseWebVoOrder getCourseInfo(@PathVariable("id")String id);
}
