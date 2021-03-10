package com.ron.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduCourse;
import com.ron.eduservice.entity.Vo.CourseFinally;
import com.ron.eduservice.entity.Vo.CourseInfo;
import com.ron.eduservice.entity.Vo.CourseQuery;
import com.ron.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
//课程管理模块
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //添加课程
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfo courseInfo){
        //需要返回课程id,后面方便前端添加课程的大纲
        String id = courseService.saveCourseInfo(courseInfo);
        return R.ok().data("courseId",id);
    }

    //主键查询课程
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId){
        //CourseInfo=课程信息+描述信息
        CourseInfo courseInfo= courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfo);
    }

    //修改课程
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfo courseInfo){
         courseService.updateCourseInfo(courseInfo);
        return R.ok();
    }


    //根据课程id查询最终确认信息
    @GetMapping("/getFinally/{courseId}")
    public R getFinally(@PathVariable("courseId") String courseId){
        CourseFinally courseFinally=courseService.getFinally(courseId);
        return R.ok().data("courseFinally",courseFinally);
    }

    //课程最终发布
    @PostMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable("id") String id){
        EduCourse course  =new EduCourse();
        course.setId(id);
        course.setStatus("Normal");//修改为发布状态
        courseService.updateById(course);//updateById修改状态忽略空值
        return R.ok();
    }


    //分页条件查询课程列表
    @PostMapping("/getCourseList/{page}/{size}")
    public R getCourseList(
            @PathVariable("page") Long page,
            @PathVariable("size") Long size,
            @RequestBody CourseQuery courseQuery){
        QueryWrapper wrapper= new QueryWrapper();
        //标题模糊查询
        String title = courseQuery.getTitle();
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }

        //起始价格
        BigDecimal beginPrice = courseQuery.getBeginPrice();
        if (!StringUtils.isEmpty(beginPrice)){
            wrapper.ge("price",beginPrice);
        }

        //最高价格
        BigDecimal endPrice = courseQuery.getEndPrice();
        if (!StringUtils.isEmpty(endPrice)){
            wrapper.le("price",endPrice);
        }
        //发布状态
        String status = courseQuery.getStatus();
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        //分页条件
        Page<EduCourse> coursePage =new Page<>(page,size);

        courseService.page(coursePage, wrapper);
        List<EduCourse> records = coursePage.getRecords();//总数居集合
        long total = coursePage.getTotal();//总数据
        return R.ok().data("list",records).data("total",total);
    }


    //删除课程
    @DeleteMapping("/{courseId}")
    public R deleteCourse(@PathVariable("courseId")String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }












}

