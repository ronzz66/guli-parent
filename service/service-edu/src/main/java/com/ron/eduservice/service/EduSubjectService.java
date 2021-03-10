package com.ron.eduservice.service;

import com.ron.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ron.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-13
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService service);

    List<OneSubject> getAllSubject();
}
