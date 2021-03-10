package com.ron.eduservice.service;

import com.ron.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
public interface EduVideoService extends IService<EduVideo> {

    //g根据课程id删除小结
    void removeVideo(String courseId);
}
