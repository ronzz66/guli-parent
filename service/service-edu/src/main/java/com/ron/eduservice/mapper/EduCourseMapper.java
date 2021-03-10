package com.ron.eduservice.mapper;

import com.ron.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ron.eduservice.entity.Vo.CourseFinally;
import com.ron.eduservice.entity.frontVo.CourseWebVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

     CourseFinally getCourseFinallyById( String courseId);

    CourseWebVo getCourseInfo(String courseId);
}
