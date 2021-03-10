package com.ron.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduCourse;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.service.EduCourseService;
import com.ron.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacherFront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService  teacherService;


    @Autowired
    private EduCourseService courseService;

    //前台分页查询老师
    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public R getTeacherList(@PathVariable("page")Long page,
                            @PathVariable("limit")Long limit){
        Page<EduTeacher> pageInfo=new Page<>(page,limit);
        Map<String,Object> map=teacherService.getTeacherFront(pageInfo);



        //返回分页信息(前台没有使用elementUi,要返回详细的分页信息)
        return R.ok().data(map);
    }


    //id查询老师
    @GetMapping("/getTeacherFrontList/{teacherId}")
    public R getTeacherInfo(@PathVariable("teacherId")Long teacherId){

        //查询讲师详情
        EduTeacher eduTeacher = teacherService.getById(teacherId);

        //查询讲师所讲的课程
        QueryWrapper<EduCourse> wrapper= new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> list = courseService.list(wrapper);


        //返回讲师详情
        return R.ok().data("teacher",eduTeacher).data("courseList",list);
    }




}

