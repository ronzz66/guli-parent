package com.ron.oss.controller;


import com.ron.commonutils.R;
import com.ron.oss.Service.Ossservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {
    //上传头像
    @Autowired
    private Ossservice ossservice;

    @PostMapping
    public R uploadOssFile(MultipartFile file){//MultipartFile获取上传头像
        //返回上传头像件路径
        String url=ossservice.uploadFile(file);
        return R.ok().data("url",url);
    }
}
