package com.ron.oss.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component                          //实现InitializingBean接口,当读取配置文件后执行方法
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    private String keyid;
    @Value("${aliyun.oss.file.keysecret}")
    private String keysecret;
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketname;

    //赋值给静态变量方便使用
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    //当读取配置文件后会执行此方法获取配置信息
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT=endpoint;
        ACCESS_KEY_ID=keyid;
        ACCESS_KEY_SECRET=keysecret;
        BUCKET_NAME=bucketname;
    }
}
