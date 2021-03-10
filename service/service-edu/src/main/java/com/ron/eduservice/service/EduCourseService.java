package com.ron.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ron.eduservice.entity.Vo.CourseFinally;
import com.ron.eduservice.entity.Vo.CourseInfo;
import com.ron.eduservice.entity.frontVo.CourseFrontVo;
import com.ron.eduservice.entity.frontVo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfo courseInfo);

    CourseInfo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfo courseInfo);

    CourseFinally getFinally(String courseId);

    void removeCourse(String courseId);

     List<EduCourse> listCourse();


    Map<String,Object> getFrontCourse(Page<EduCourse> pageInfo, CourseFrontVo courseFrontVo);

    CourseWebVo getFrontCourseInfo(String courseId);
}
