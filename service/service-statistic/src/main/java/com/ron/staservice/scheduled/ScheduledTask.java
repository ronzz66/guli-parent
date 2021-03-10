package com.ron.staservice.scheduled;

import com.ron.staservice.service.StatisticsDailyService;
import com.ron.staservice.utils.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component//定时任务
public class ScheduledTask {

    private StatisticsDailyService  statisticsDailyService;
    //每天1点,自动统计记录
    @Scheduled(cron = "0 0 1 * * ? ")//springboot只有6位最后一位年默认
    public void AutoCreate(){
        //获取前一天日期字符串
        Date date = DateUtil.addDays(new Date(), -1);
        String s = DateUtil.formatDate(date);


        statisticsDailyService.registerCount(s);
    }
}
