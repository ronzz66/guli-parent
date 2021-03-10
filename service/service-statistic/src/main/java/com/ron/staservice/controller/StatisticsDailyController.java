package com.ron.staservice.controller;


import com.ron.commonutils.R;
import com.ron.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-24
 */
@RestController
@RequestMapping("/staservice/statistics-daily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    //统计一天的注册人数,生成记录
    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable("day")String day){

        staService.registerCount(day);
        return R.ok();

    }
    //类型:注册人数/登录人数/每日新增课程数/每日播放视频数
    //指定查询类型,查询在时间范围内该类型的数量.
    //图标显示:返回日期数组,和数量数组
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable("type")String type,//统计类型
                      @PathVariable("begin")String begin,//开锁日期
                      @PathVariable("end")String end){//结束日期

        Map<String,Object> map=staService.getShowData(type,begin,end);
        return R.ok().data(map);

    }


}

