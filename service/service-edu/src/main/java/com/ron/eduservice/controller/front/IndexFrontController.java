package com.ron.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduCourse;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.service.EduCourseService;
import com.ron.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {//首页展示


    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService eduTeacherService;



    //查询8个热门课程+4个名师
    @GetMapping("/index")
    public R getIndex(){
        //查询8个课程
        List<EduCourse> list = courseService.listCourse();
        //查询4个名师
        List<EduTeacher> list2 = eduTeacherService.listTeacher();
        return R.ok().data("eduList",list).data("teacherList",list2);
    }
}
