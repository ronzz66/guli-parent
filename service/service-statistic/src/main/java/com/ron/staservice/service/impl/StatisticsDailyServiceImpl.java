package com.ron.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.commonutils.R;
import com.ron.staservice.entity.StatisticsDaily;
import com.ron.staservice.feign.UcenterClient;
import com.ron.staservice.mapper.StatisticsDailyMapper;
import com.ron.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-24
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired//统计一天注册人数:查询一天的注册人数,添加记录
    private UcenterClient ucenterClient;
    @Override
    public void registerCount(String day) {
        //在添加记录时删除今天的记录
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);
        //远程调用查询一天的注册人数
        R r = ucenterClient.countRegister(day);
        Integer count =(Integer)r.getData().get("countRegister");
        //把数据存入统计表中
        StatisticsDaily statisticsDaily=new StatisticsDaily();
        statisticsDaily.setRegisterNum(count);//注册人数
        statisticsDaily.setDateCalculated(day);//统计日期

        //模拟,观看人数,购买人数,登录人数
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,200));//登录
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,200));//观看
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,200));//每日新增课程数
        baseMapper.insert(statisticsDaily);


    }

    //图标显示,查询数据返回
    @Override                                   //type为查询类型
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.select("date_calculated",type);//查询日期和  注册人数/登录人数/每日新增课程数/每日播放视频数 中的一个的记录
        wrapper.between("date_calculated",begin,end);//范围查询
        List<StatisticsDaily> dailies = baseMapper.selectList(wrapper);

        //需要返回前端的数组json类型,对应后端的list
        //返回日期集合
        List<String> dateList = dailies.stream().map(daily -> {
            String date = daily.getDateCalculated();
            return date;
        }).collect(Collectors.toList());
        //返回查询注册人数/登录人数/每日新增课程数/每日播放视频数 的数量集合
        List<Integer> numDataList = dailies.stream().map(daily -> {
            Integer typeData = 0;
            //获取查询类型的数量
            switch (type) {
                case "register_num": //查询注册:
                    typeData = daily.getRegisterNum();
                    break;
                case "login_num":  //查询登录
                    typeData = daily.getLoginNum();
                    break;
                case "video_view_num":  //查询视频观看的人数
                    typeData = daily.getVideoViewNum();
                    break;
                case "course_num":  //查询课程购买数
                    typeData = daily.getCourseNum();
                    break;
            }
            return typeData;
        }).collect(Collectors.toList());
        //封装为map
        Map map = new HashMap();
        map.put("dateList",dateList);//查询日期集合
        map.put("numDataList",numDataList);//查询选择类型的数量集合
        return map;
    }
}
