package com.ron.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.eduservice.entity.EduSubject;
import com.ron.eduservice.entity.excel.SubjectData;
import com.ron.eduservice.entity.subject.OneSubject;
import com.ron.eduservice.entity.subject.TwoSubject;
import com.ron.eduservice.listenenr.SubjectExcelListener;
import com.ron.eduservice.mapper.EduSubjectMapper;
import com.ron.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-13
 */
//读取excel,写入数据库
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService service) {
        try {
            InputStream inputStream = file.getInputStream();//获取文件输入流
            //读取excel的数据
                //inputStream文件流
                //SubjectData:表映射信息
                //SubjectExcelListener:监听(传递EduSubjectService执行数据库操作)
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(service)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //获取所有的课程分类
    @Override
    public List<OneSubject> getAllSubject() {

        //查询所有的一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
            //baseMapper在ServiceImpl存在,继承可直接使用
        List<EduSubject> oneSubjects = baseMapper.selectList(wrapperOne);//或者使用this.list(wrapper)//底层也是baseMapper;
        //查询所有的二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjects = baseMapper.selectList(wrapperTwo);

        //封装一级分类
            //遍历查询的一级集合,转换成页面显示格式的一级分类集合(OneSubject实体类)
        List<OneSubject> oneSubjectList = oneSubjects.stream().map(edu -> {
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(edu,oneSubject);//copy一个对象的属性,到另一个对象,不需要字段都相同
            return oneSubject;
        }).collect(Collectors.toList());


        //封装二级分类到一级分类的Children
            //遍历转换好的一级分类
        oneSubjectList.forEach(oneSubject -> {
                //遍历查询出的二级分类集合
            for (EduSubject twoSubject : twoSubjects) {
                if (twoSubject.getParentId().equals(oneSubject.getId())){//当二级分类的父id=封装好的二级分类的id
                    TwoSubject twoSub = new TwoSubject();//转换成最终显示的格式(TwoSubject)
                    BeanUtils.copyProperties(twoSubject,twoSub);
                    oneSubject.getChildren().add(twoSub);//封装到一级分类的children
                }
            }
        });
        return oneSubjectList;
    }
}
