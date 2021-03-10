package com.ron.controller;


import com.ron.commonutils.R;
import com.ron.service.MsmService;
import com.ron.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edu/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //发送短信
    @GetMapping("/send/{phoneId}")
    public R sendMsm(@PathVariable("phoneId")String phoneId){


        //1.先查看redis,判断是否已经发送过了
        String code = redisTemplate.opsForValue().get(phoneId);
        if (!StringUtils.isEmpty(code)){//如果redis不为空,则直接返回
            return R.ok();
        }
        //2.未发送
        code = RandomUtil.getFourBitRandom();//获取四位随机数
        Map<String,Object> param=new HashMap<>();
        param.put("code",code);
        //3.调用service发送短信
        boolean flag = msmService.send(param,phoneId);

        if (flag){
            //4.发送成功后,把验证码放入redis
            redisTemplate.opsForValue().set(phoneId,code,5, TimeUnit.MINUTES);//5分钟过期
            return R.ok();
        }else {
            return R.error();
        }

    }

}
