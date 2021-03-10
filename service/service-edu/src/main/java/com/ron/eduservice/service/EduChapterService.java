package com.ron.eduservice.service;

import com.ron.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ron.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterById(String courseId);

    boolean deleteChapter(String id);

    void removeChapter(String courseId);
}
