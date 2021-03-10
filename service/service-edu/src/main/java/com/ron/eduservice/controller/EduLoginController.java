package com.ron.eduservice.controller;



import com.ron.commonutils.R;
import org.springframework.web.bind.annotation.*;

//登录模块
@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin
public class EduLoginController {


    //login
    @PostMapping("/login")
    public R login(){

        return R.ok().data("token","admin");
    }

    //info
    @GetMapping("/info")
    public R info(){

        return R.ok().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4286790538,1794958079&fm=26&gp=0.jpg");
    }
}
