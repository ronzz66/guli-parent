package com.ron.vod.service.imol;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.ron.exceptionhandler.GuliException;
import com.ron.vod.service.VodService;
import com.ron.vod.utils.ConstantVodPropertiesUtils;
import com.ron.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService{

    //上传视频
    @Override
    public String uploadVideo(MultipartFile file) {
        //accessKeyId:keyid
        String accessKeyId= ConstantVodPropertiesUtils.ACCESS_KEY_ID;
        //accessKeySecret:key密码
        String accessKeySecret= ConstantVodPropertiesUtils.ACCESS_KEY_SECRET;

        //fileName:上传文件原始名称
        String fileName=file.getOriginalFilename();
        //title:上传到阿里云的文件名称
        int i = fileName.lastIndexOf(".");
        String title = fileName.substring(0, i);

        //inputStream:文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID

        String videoId =response.getVideoId();//获取文件上传id

        return videoId;
    }

    //根据视频id集合删除多个视频
    @Override
    public void removeVideoList(List videoIdList) {
        try {
            //初始化请求客户端
            DefaultAcsClient client = InitVodClient.initVodClient
                    (ConstantVodPropertiesUtils.ACCESS_KEY_ID,
                            ConstantVodPropertiesUtils.ACCESS_KEY_SECRET);
            //创建删除请求
            DeleteVideoRequest request = new DeleteVideoRequest();
            //StringUtils join方法用,分隔集合
            String join = StringUtils.join(videoIdList.toArray(), ",");
            //删除多个视频
            request.setVideoIds(join);
            //调用客户端删除
            client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }


    public static void main(String[] args) {

    }
}
