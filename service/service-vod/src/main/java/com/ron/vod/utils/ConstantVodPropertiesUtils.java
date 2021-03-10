package com.ron.vod.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component                          //实现InitializingBean接口,当读取配置文件后执行方法
public class ConstantVodPropertiesUtils implements InitializingBean {
    //读取配置文件
    private String endpoint;
    @Value("${aliyun.vod.file.keyid}")
    private String keyid;
    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;

    //赋值给静态变量方便使用
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    //当读取配置文件后会执行此方法获取配置信息
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID=keyid;
        ACCESS_KEY_SECRET=keysecret;
    }
}
