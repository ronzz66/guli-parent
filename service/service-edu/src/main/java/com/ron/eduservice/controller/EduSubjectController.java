package com.ron.eduservice.controller;


import com.ron.commonutils.R;
import com.ron.eduservice.entity.subject.OneSubject;
import com.ron.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-13
 */
//课程分类模块
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //根据Excel表格执行添加课程分类
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){//MultipartFile传递file文件

        //传递EduSubjectService给EasyExcel
        eduSubjectService.saveSubject(file,eduSubjectService);

        return R.ok();
    }


    //课程分类的树形结构
    @GetMapping("/getAllSubject")
    public R getAllSubject(){

        //返回页面需要的一级分类集合
        List<OneSubject> list=eduSubjectService.getAllSubject();
        return R.ok().data("list",list);
    }


}

