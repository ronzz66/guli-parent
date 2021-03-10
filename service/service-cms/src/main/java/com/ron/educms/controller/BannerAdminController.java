package com.ron.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.R;
import com.ron.educms.entity.CrmBanner;
import com.ron.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

//后台管理员使用的
@RestController
@RequestMapping("/educms/bannerAdmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;
    //分页查询banner
    @GetMapping("/{page}/{limit}")
    public R pageBanner(@PathVariable("page")Long page,
                        @PathVariable("limit")Long limit){
        Page<CrmBanner> bannerPage=new Page<>(page,limit);

        crmBannerService.page(bannerPage,null);
        List<CrmBanner> records = bannerPage.getRecords();
        long total = bannerPage.getTotal();
        return R.ok().data("items",records).data("total",total);
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "修改Banner")
    @PostMapping("/addbanner")
    public R save(@RequestBody CrmBanner banner) {
        crmBannerService.save(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable String id) {

        crmBannerService.removeById(id);
        return R.ok();
    }

}

