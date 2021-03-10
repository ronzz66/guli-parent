package com.ron.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.ron.commonutils.R;
import com.ron.exceptionhandler.GuliException;
import com.ron.vod.service.VodService;
import com.ron.vod.utils.ConstantVodPropertiesUtils;
import com.ron.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;
    //上传视频到阿里云
    @PostMapping("/uploadAli")
    public R uploadAli( MultipartFile file){
        String videoId=vodService.uploadVideo(file);
        return R.ok().data("videoId",videoId);

    }


    //根据视频id删除阿里云视频
    @DeleteMapping("/removeVideo/{id}")
    public R removeVideo( @PathVariable("id") String id){
        try {
            //初始化请求客户端
            DefaultAcsClient client = InitVodClient.initVodClient
                    (ConstantVodPropertiesUtils.ACCESS_KEY_ID,
                            ConstantVodPropertiesUtils.ACCESS_KEY_SECRET);
            //创建删除请求
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(id);
            //调用客户端删除
            client.getAcsResponse(request);
            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }

    }

    //删除多个视频的方法
    @DeleteMapping("/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){

        vodService.removeVideoList(videoIdList);
        return R.ok();
    }

    //根据视频id获取视频凭证
    @GetMapping("/getPalyAuth/{id}")
    public R getPalyAuth(@PathVariable("id") String id){
        //初始化client对象
        DefaultAcsClient client = InitVodClient.initVodClient(
                ConstantVodPropertiesUtils.ACCESS_KEY_ID,
                ConstantVodPropertiesUtils.ACCESS_KEY_SECRET);
        //创建reuqest对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        //设置视频id
        request.setVideoId(id);
        //调用方法获取凭证
        try {
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuliException(20001,"获取凭证失败");
        }
    }

}
