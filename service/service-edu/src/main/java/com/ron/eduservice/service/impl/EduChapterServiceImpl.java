package com.ron.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.eduservice.entity.EduChapter;
import com.ron.eduservice.entity.EduVideo;
import com.ron.eduservice.entity.chapter.ChapterVo;
import com.ron.eduservice.entity.chapter.VideoVo;
import com.ron.eduservice.feign.VodClitent;
import com.ron.eduservice.mapper.EduChapterMapper;
import com.ron.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ron.eduservice.service.EduVideoService;
import com.ron.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;//小结的service


    //查询所有的章节和小节

    @Override
    public List<ChapterVo> getChapterById(String courseId) {
        //根据课程id查询章节
        QueryWrapper<EduChapter> wrapper= new QueryWrapper();
        wrapper.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(wrapper);

        //根据课程id查询小结
        QueryWrapper<EduVideo> eduVideoWrapper= new QueryWrapper();
        wrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = eduVideoService.list(eduVideoWrapper);

        //转换为页面需要的ChapterVo
        List<ChapterVo> chapterVos = eduChapters.stream().map(eduChapter -> {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            for (EduVideo eduVideo : eduVideos) {
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();

                    BeanUtils.copyProperties(eduVideo,videoVo);
                    chapterVo.getChildren().add(videoVo);
                }
            }
            return chapterVo;
        }).collect(Collectors.toList());
        return chapterVos;
    }

    //删除章节的方法,同时判断是否存在小结
    @Override
    public boolean deleteChapter(String id) {
        //查询该章节是否还有小结,如果没有才能删除
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);//根据章节id查询小结
        int count = eduVideoService.count(queryWrapper);
        if (count>0){
            throw new GuliException(20001,"不能删除");
        }else {//当章节下没有小结时才删除章节
            int i = baseMapper.deleteById(id);
            return i>0;
        }
    }

    //根据课程id删除章节
    @Override
    public void removeChapter(String courseId) {


        //根据课程id删除章节
        QueryWrapper<EduChapter> wrapper =new QueryWrapper();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }

}
