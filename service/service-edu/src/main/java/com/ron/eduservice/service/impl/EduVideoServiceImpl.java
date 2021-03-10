package com.ron.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.eduservice.entity.EduChapter;
import com.ron.eduservice.entity.EduVideo;
import com.ron.eduservice.feign.VodClitent;
import com.ron.eduservice.mapper.EduVideoMapper;
import com.ron.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired//远程调用,根据视频id集合删除视频的方法
    private VodClitent vodClitent;

    //根据课程id删除小结集合
    //先删除小结对应的视频
    //再删除小结
    @Override
    public void removeVideo(String courseId) {
        //1.根据课程id删除小结里面的视频
        //查询根据课程id查询小结
        QueryWrapper<EduVideo> queryWrapper =new QueryWrapper();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");//指定查询视频id列
        List<EduVideo> videos = baseMapper.selectList(queryWrapper);
        //转换为小结视频id集合
        List<String> list = videos.stream().map(video -> {
            String sourceId = video.getVideoSourceId();
            return sourceId;
        }).filter(x->x!=null).collect(Collectors.toList());

        if (list.size()>0){
            //远程调用,根据视频id集合删除视频
            vodClitent.deleteBatch(list);
        }


        //2.根据课程id删除小结
        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }



}
