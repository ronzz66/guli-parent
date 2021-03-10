package com.ron.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-09
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> listTeacher();

    Map<String,Object> getTeacherFront(Page<EduTeacher> pageInfo);
}
