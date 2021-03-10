package com.ron.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.entity.Vo.TeacherQuery;
import com.ron.eduservice.service.EduTeacherService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-09
 */

//讲师模块
@RestController
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin
@Api(tags = "讲师管理")
public class EduTeacherController {



    @Autowired
    private EduTeacherService eduTeacherService;


    //查询所有讲师
    @ApiOperation(value = "查询所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);

        return R.ok().data("items",list);
    }
    //根据id查询讲师
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("/getTeacher/{id}")
    public R findTeacherById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable("id")String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);

        return R.ok().data("teacher",eduTeacher);
    }

    //修改讲师信息
    @ApiOperation(value = "修改讲师信息")
    @PostMapping("/updateTeacher")
    public R updateTeacher(
            @ApiParam(name = "修改讲师", value = "EduTeacher", required = true)
            @RequestBody EduTeacher eduTeacher){

        boolean b = eduTeacherService.updateById(eduTeacher);

        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }





    //根据id逻辑删除
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping(value = "/{id}")
    public R deletelTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable("id")String id){
        boolean b = eduTeacherService.removeById(id);
        System.out.println(b);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师
    @ApiOperation(value = "分页查询")
    @GetMapping("/pageTeacher/{page}/{size}")
    public R findpageTeacher(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "size", value = "每页显示数据数", required = true)
            @PathVariable("size")Long size){
        //分页对象
        Page<EduTeacher> pageTeacher=new Page<>(page,size);

        //分页查询粉装进分页对象
        eduTeacherService.page(pageTeacher, null);



        long total = pageTeacher.getTotal();//获取总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//获取分页集合
        return R.ok().data("total",total).data("rows",records);
    }

    //条件分页查询
    @ApiOperation(value = "条件分页查询")
    @PostMapping("/pageTeacherCondition/{page}/{size}")
    public R findTeacherCondition(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable("page")Long page,

            @ApiParam(name = "size", value = "每页显示数据数", required = true)
            @PathVariable("size")Long size,

            @ApiParam(name = "teacherQuery", value = "查询条件对象", required = false)
            @RequestBody(required = false)
            TeacherQuery teacherQuery){

        //分页对象
        Page<EduTeacher> pageTeacher=new Page<>(page,size);

        //查询条件根据条件值查询
        QueryWrapper<EduTeacher> queryWrapper=new QueryWrapper();

        //教师姓名模糊查询
        String name = teacherQuery.getName();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }

        //教师等级
        Integer level = teacherQuery.getLevel();
        if (!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }

        //注册时间>=
        String begin = teacherQuery.getBegin();
        if (!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }

        //注册时间<=
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_create",end);
        }

        //时间降序排序条件
        queryWrapper.orderByDesc("gmt_create");

        //分页条件查询
        eduTeacherService.page(pageTeacher,queryWrapper);

        long total = pageTeacher.getTotal();//获取总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//获取分页集合
        return R.ok().data("total",total).data("rows",records);
    }




    //添加讲师
    @ApiOperation(value = "添加讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(
            @ApiParam(name = "eduTeacher", value = "添加讲师", required = true)
            @RequestBody(required = true) EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }


    }


}

