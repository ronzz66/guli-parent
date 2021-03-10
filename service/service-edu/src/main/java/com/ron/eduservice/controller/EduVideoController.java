package com.ron.eduservice.controller;


import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduVideo;
import com.ron.eduservice.feign.VodClitent;
import com.ron.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@RestController
@RequestMapping("/eduservice/edu-video")
//@CrossOrigin
//小结模块
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;
    @Autowired//远程调用删除小节视频的service
    private VodClitent vodClitent;

    //添加小结
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();

    }
    //根据小结id删除小结
    //TODO 后面还要继续删除小结里面的视频
    @DeleteMapping("/deleteVideo/{id}")
    public R deleteVideo(@PathVariable("id") String id){


        //查询要删除的小结id
        EduVideo eduVideo = eduVideoService.getById(id);
        //获取小结的视频id
        String sourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(sourceId)){
            //远程调用实现根据视频id删除视频
            vodClitent.removeVideo(sourceId);
        }
        //删除小结
        eduVideoService.removeById(id);
        return R.ok();

    }
    //修改小结
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();

    }

}

