package com.ron;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class VodTest {
    public static void main(String[] args) throws ClientException {

//        //上传视频的方法
//        String accessKeyId="LTAI4GEYSSivCawpgPUeR3PG";
//        String accessKeySecret="iyENoW1eMXCZWUKgdMDT0GtLnrQH0n";//
//        String title="What If I Want to Move Fasterby";//上传文件的名称
//        String fileName="F:/1.mp4";//本地文件的路径和名称
//        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
//        request.setPartSize(2 * 1024 * 1024L);
//        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
//        request.setTaskNum(1);
//
//        UploadVideoImpl uploader = new UploadVideoImpl();
//        UploadVideoResponse response = uploader.uploadVideo(request);
//
//        if (response.isSuccess()) {
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//        } else {
//            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//            System.out.print("ErrorCode=" + response.getCode() + "\n");
//            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
//        }
        getPlayAuth();


    }


    //根据视频id获取视频凭证,凭证可以解决加密的视频无法播放
    public static void getPlayAuth() throws ClientException {
        //根据视频id获取视频凭证
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient
                ("LTAI4GEYSSivCawpgPUeR3PG", "iyENoW1eMXCZWUKgdMDT0GtLnrQH0n");

        //获取视频凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        //向request设置视频id
        request.setVideoId("182ae951fa364dedba57d5ba488b8bd3");

        //调用初始化对象,传递request,获取数据
        response=client.getAcsResponse(request);
        System.out.println(response.getPlayAuth());
    }

    //根据视频id获取播放地址
    public static void getPlayUrl() throws ClientException {
        //创建初始化对象
        DefaultAcsClient defaultAcsClient = InitObject.initVodClient
                ("LTAI4GEYSSivCawpgPUeR3PG", "iyENoW1eMXCZWUKgdMDT0GtLnrQH0n");
        //创建获取视频的request和response
        GetPlayInfoRequest infoRequest = new GetPlayInfoRequest();
        GetPlayInfoResponse infoResponse = new GetPlayInfoResponse();
        //向request设置视频id
        infoRequest.setVideoId("74c9be84c98b4c83a5a639179df4dd5e");
        //调用初始化对象,传递request,获取数据
        infoResponse=defaultAcsClient.getAcsResponse(infoRequest);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = infoResponse.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + infoResponse.getVideoBase().getTitle() + "\n");
    }

}
