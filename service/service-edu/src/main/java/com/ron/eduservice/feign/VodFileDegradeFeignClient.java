package com.ron.eduservice.feign;

import com.ron.commonutils.R;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component//降级方法
public class VodFileDegradeFeignClient implements VodClitent {


    public R removeVideo(String id){
        return R.error().message("删除阿里云视频失败");

    }

    public R deleteBatch(List<String> videoIdList){
        return R.error().message("删除多个阿里云视频失败");
    }
}
