package com.ron.eduservice.feign;


import com.ron.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClitent {


    //根据视频id删除阿里云视频
    @DeleteMapping("/eduvod/video/removeVideo/{id}")
    public R removeVideo(@PathVariable("id") String id);

    //根据视频id集合删除多个视频的方法
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}

