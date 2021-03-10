package com.ron.educenter.controller;


import com.google.gson.Gson;
import com.ron.commonutils.JwtUtils;
import com.ron.commonutils.R;
import com.ron.educenter.entity.UcenterMember;
import com.ron.educenter.service.UcenterMemberService;
import com.ron.educenter.utils.ConstantWXUtiils;
import com.ron.educenter.utils.HttpClientUtils;
import com.ron.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WXApicontroller {


    @Autowired
    private UcenterMemberService UMservice;

    @GetMapping("/callback")
    public String callback(String code,String state){
        try {
            //code为临时票据，类似于验证码
            //固定地址
            String baseAccessTokeenUri="https://api.weixin.qq.com/sns/oauth2/access_token"+
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String tokenUrl = String.format(baseAccessTokeenUri,
                    ConstantWXUtiils.WX_OPEN_APP_ID,//微信appid
                    ConstantWXUtiils.WX_OPEN_APP_SECRET,//微信密钥
                    code);//扫码获取的code
            //1.拿着code请求固定地址，得到两个值access_token和openid
            //使用httpClient发送请求(httpClient不用浏览器也可以得到请求结果),得到返回结果(返回结果为json类型)
            String tokenInfo = HttpClientUtils.get(tokenUrl);
            Gson gson=new Gson();
            HashMap infoMap = gson.fromJson(tokenInfo, HashMap.class);//转换为map
            String accse_token = (String)infoMap.get("access_token");
            String openid = (String) infoMap.get("openid");//openid(每个微信唯一)

            //2.查询数据库,判断数据库是否存在该微信用户
            UcenterMember ucenterMember= UMservice.getByIdOpenId(openid);
            if (ucenterMember==null){//如果为null,则请求微信资源,获取信息

                //3.访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accse_token,
                        openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);//httpClient发送请求获取微信用户信息
                Gson userGson=new Gson();
                HashMap userMap = userGson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userMap.get("nickname");//微信头像
                String headimgurl = (String) userMap.get("headimgurl");//微信昵称


                //4.把扫描人加入数据库
                ucenterMember=new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setNickname(nickname);
                ucenterMember.setAvatar(headimgurl);
                UMservice.save(ucenterMember);
            }

            //使用JWT根据member对象生成tokenzfc
            String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

            return  "redirect:http://localhost:3000?token="+jwtToken;//把token拼接在首页路径上
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"登录失败");
        }
    }


    //生成微信二维码
    @GetMapping("/login")
    public String genWXcode(HttpSession session) {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +//%s相当于占位符
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl=ConstantWXUtiils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");//编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                ConstantWXUtiils.WX_OPEN_APP_ID,//微信id
                redirectUrl,//重定向地址
                "ronron"//任意
        );
        return "redirect:"+url;
    }
}