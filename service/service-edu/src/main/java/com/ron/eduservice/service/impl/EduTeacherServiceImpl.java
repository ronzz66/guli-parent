package com.ron.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.mapper.EduTeacherMapper;
import com.ron.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-09
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //查询四个老师
    @Override
    @Cacheable(key = "'selectTeacherList'",value = "teacher")
    public List<EduTeacher> listTeacher() {
        QueryWrapper<EduTeacher> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.orderByDesc("id");
        queryWrapper2.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(queryWrapper2);
        return eduTeachers;
    }





    //查询前台老师集合
    @Override
    public Map<String, Object> getTeacherFront(Page<EduTeacher> pageParam) {
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        baseMapper.selectPage(pageParam,null);

        Map<String,Object> hashMap=new HashMap<>();
        List<EduTeacher> records = pageParam.getRecords();//老师集合
        long current = pageParam.getCurrent();//当前页
        long pages = pageParam.getPages();//总页数
        long size = pageParam.getSize();//每页记录数
        long total = pageParam.getTotal();//总数据数
        boolean hasNext = pageParam.hasNext();//是否有下一页
        boolean hasPrevious = pageParam.hasPrevious();//是否有上一页

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
}
