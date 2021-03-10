package com.ron.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.eduservice.entity.EduCourse;
import com.ron.eduservice.entity.EduCourseDescription;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.entity.Vo.CourseFinally;
import com.ron.eduservice.entity.Vo.CourseInfo;
import com.ron.eduservice.entity.frontVo.CourseFrontVo;
import com.ron.eduservice.entity.frontVo.CourseWebVo;
import com.ron.eduservice.mapper.EduCourseMapper;
import com.ron.eduservice.service.EduChapterService;
import com.ron.eduservice.service.EduCourseDescriptionService;
import com.ron.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ron.eduservice.service.EduVideoService;
import com.ron.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired//调用课程描述service
    private EduCourseDescriptionServiceImpl courseDescriptionService;

    @Autowired//小结service
    private EduVideoService eduVideoService;

    @Autowired//章节service
    private EduChapterService eduChapterService;

    //添加课程信息和描述
    @Override
    public String saveCourseInfo(CourseInfo courseInfo) {
        //1.向课程表添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo,eduCourse);//把前端对象转换为数据库需要的类型
        int i = baseMapper.insert(eduCourse);
        if (i<=0){
            //添加失败
            throw new GuliException(20001,"添加课程失败");
        }
        String cid =eduCourse.getId();//获取上传后的id
        //2.向简介表添加描述
        EduCourseDescription courseDescription=new EduCourseDescription();
        courseDescription.setDescription(courseInfo.getDescription());
        courseDescription.setId(cid);//设置描述课程id为课程id,确保两表为同一id
        courseDescriptionService.save(courseDescription);
        return cid;//返回课程id

    }

    //主键查询课程
    @Override
    public CourseInfo getCourseInfo(String courseId) {
        //主键查询课程
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //主键查询课程简介
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        System.out.println(eduCourse);
        //封装返回页面对象
        CourseInfo courseInfo= new CourseInfo();
        if (eduCourse!=null){
            //封装描述信息
            BeanUtils.copyProperties(eduCourse,courseInfo);
        }
        courseInfo.setDescription(courseDescription.getDescription());
        return courseInfo;
    }

    //修改课程方法
    @Override
    public void updateCourseInfo(CourseInfo courseInfo) {
        //修改课程
        EduCourse eduCourse =new EduCourse();
        BeanUtils.copyProperties(courseInfo,eduCourse);
        int updateById = baseMapper.updateById(eduCourse);
        if (updateById==0){
            throw  new GuliException(20001,"修改失败");
        }

        //修改描述
        EduCourseDescription description=new EduCourseDescription();
        BeanUtils.copyProperties(courseInfo,description);
        courseDescriptionService.updateById(description);

    }

    //最终查询方法
    @Override
    public CourseFinally getFinally(String courseId) {
        CourseFinally courseFinally = baseMapper.getCourseFinallyById(courseId);
        return courseFinally;
    }


    //删除课程方法
    @Override
    public void removeCourse(String courseId) {
        //1.根据课程id删除小结
        eduVideoService.removeVideo(courseId);

        //2.根据课程id删除章节
        eduChapterService.removeChapter(courseId);
        //3.根据课程id删除描述
        courseDescriptionService.removeById(courseId);
        //4.根据课程id删除课程
        int i = baseMapper.deleteById(courseId);
        if (i==0){
            throw new GuliException(2001,"删除失败");
        }

    }

    //查询8个课程
    @Override
    @Cacheable(key = "'selectCourseList'",value = "course")
    public List<EduCourse> listCourse() {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 8");
        List<EduCourse> courses = baseMapper.selectList(queryWrapper);
        return courses;
    }

    //前台条件查询分页课程
    @Override
    public Map<String, Object> getFrontCourse(Page<EduCourse> pageInfo, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper= new QueryWrapper<>();

        //判断条件是否为空,不为空添加条件
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){//判断一级分类是否为null
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())){//判断二级分类是否为null
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }

        //判断排序
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){//判断销量排序
            wrapper.orderByDesc("buy_count",courseFrontVo.getBuyCountSort());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {//最新排序
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {//价格排序
            wrapper.orderByDesc("price");
        }
        baseMapper.selectPage(pageInfo,wrapper);//查询

        //封装map
        Map<String,Object> hashMap=new HashMap<>();
        List<EduCourse> records = pageInfo.getRecords();//课程集合
        long current = pageInfo.getCurrent();//当前页
        long pages = pageInfo.getPages();//总页数
        long size = pageInfo.getSize();//每页记录数
        long total = pageInfo.getTotal();//总数据数
        boolean hasNext = pageInfo.hasNext();//是否有下一页
        boolean hasPrevious = pageInfo.hasPrevious();//是否有上一页

        //存入map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    //查询课程详情页面
    @Override
    public CourseWebVo getFrontCourseInfo(String courseId) {

        CourseWebVo courseWebVo=baseMapper.getCourseInfo(courseId);
        return courseWebVo;
    }
}
