package com.ron.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.R;
import com.ron.educms.entity.CrmBanner;
import com.ron.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
//前台的用户
@RestController
@RequestMapping("/educms/bannerFront")
@CrossOrigin
public class BannerFrontController {

    //查询所有的Banner轮播图
    @Autowired
    private CrmBannerService annerService;

    @GetMapping("/getAllBanner")
    public R getAllBanner(){

        List<CrmBanner> banners= annerService.selectAllBanner();
        return R.ok().data("list",banners);
    }

}

