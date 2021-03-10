package com.ron.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.ron.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service
public class MsmServiceImpl implements MsmService {


    //发送短信
    @Override
    public boolean send(Map<String, Object> param, String phoneId) {

        if(StringUtils.isEmpty(phoneId)) return false;

        //设置id和密钥
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4GEYSSivCawpgPUeR3PG", "iyENoW1eMXCZWUKgdMDT0GtLnrQH0n");
        //初始化client
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关参数不用改
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关参数
        request.putQueryParameter("PhoneNumbers", phoneId);//手机号
        request.putQueryParameter("SignName", "乐优商城");//签名名称
        request.putQueryParameter("TemplateCode", "SMS_198922772");//模板id
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));//验证码,要求json格式{"code":"xx验证码"}


        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            //判断是否发送成功
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;

    }
}
