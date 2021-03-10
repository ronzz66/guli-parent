package com.ron.educenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.commonutils.JwtUtils;
import com.ron.commonutils.R;
import com.ron.commonutils.orderVo.UcenterMemberOrder;
import com.ron.educenter.entity.UcenterMember;
import com.ron.educenter.entity.Vo.RegisterVo;
import com.ron.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-20
 */
@RestController
@RequestMapping("/educenter/ucenter-member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;


    //登录方法
    @PostMapping("/login")
    public R login(@RequestBody UcenterMember ucenterMember){

        //调用service登录方法,返回jwt生成token信息
        String token=ucenterMemberService.login(ucenterMember);

        return R.ok().data("token",token);
    }


    //注册方法
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){

        //调用service注册
        ucenterMemberService.register(registerVo);

        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R registerUser(HttpServletRequest request){

        //JWT工具类,根据token获取用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //调用根据id查询用户方法
        UcenterMember member = ucenterMemberService.getById(id);
        return R.ok().data("userInfo",member);
    }
    //根据用户id查询用户,返回订单的用户
    @GetMapping("/getUserByid/{id}")
    public UcenterMemberOrder getUserByid(@PathVariable("id")String id){


        //调用根据id查询用户方法
        UcenterMember member = ucenterMemberService.getById(id);
        //返回用户信息
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    //查询一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable("day")String day){

        Integer count=ucenterMemberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }

}

