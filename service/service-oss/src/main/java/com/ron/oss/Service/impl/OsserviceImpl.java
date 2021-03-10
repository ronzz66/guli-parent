package com.ron.oss.Service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ron.oss.Service.Ossservice;
import com.ron.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OsserviceImpl implements Ossservice{
    @Override
    public String uploadFile(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        //accessKeyId 账号
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        //accessKeySecret 密码
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        //存储库的名称
        String bucketname = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。//地域节点，accessKeyId,accessKey密码
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = null;
        String url = null;//返回url对象
        try {
            //获取文件的输入流
            inputStream = file.getInputStream();
            //生成唯一id给文件
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //获取文件的名称
            String filename = file.getOriginalFilename();
            filename=uuid+filename;//拼接文件名和uuid,生成唯一文件名

            /**
             * 执行上传(bucketname:库名
             *         filename:路径+名称.不填路径为根目录
             *         inputStream:文件输入流
             */
            //创建目录,用日期作为目录. joda工具类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename=datePath+"/"+filename;//拼接目录和文件名
            ossClient.putObject(bucketname, filename, inputStream);
            //把上传后的路径返回,路径的规则为 存储库.地域节点/文件名称
                //https://myedu10.oss-cn-shanghai.aliyuncs.com/logo.png
            url = "https://"+bucketname+"."+endpoint+"/"+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        return url;
    }
}
