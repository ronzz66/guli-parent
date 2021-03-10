package com.ron.eduservice.listenenr;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.eduservice.entity.EduSubject;
import com.ron.eduservice.entity.excel.SubjectData;
import com.ron.eduservice.service.EduSubjectService;
import com.ron.exceptionhandler.GuliException;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    public EduSubjectService subjectService;//构造方法需要把EduSubjectService传递过来,执行数据库添加分类的操作

    public SubjectExcelListener() {
    }
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override//读取excel每行数据  :没行的数据格式为:   一级分类:二级分类(前端开发:JavaScript)
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData==null){//文件为空,抛出异常
            throw  new GuliException(20001,"文件为空");
        }
        //判断表中是否有该一级分类:不重复添加
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (existOneSubject==null){//如果为空则往数据库添加
            existOneSubject=new EduSubject();
            existOneSubject.setParentId("0");//父id设置为0
            String oneSubjectName = subjectData.getOneSubjectName();//获取一级分类名
            existOneSubject.setTitle(oneSubjectName);//设置一级分类名
            subjectService.save(existOneSubject);//执行添加
        }
        //添加二级分类
        //获取一级分类的id值
        String pid=existOneSubject.getId();
        //查询二级分类是否存在
        EduSubject twoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(),pid);
        if (twoSubject==null){//如果为空则往数据库添加
            existOneSubject=new EduSubject();
            existOneSubject.setParentId(pid);//父id设置为0
            String twoSubjectName = subjectData.getTwoSubjectName();//获取二级分类名
            existOneSubject.setTitle(twoSubjectName);//设置二级分类名
            subjectService.save(existOneSubject);//执行添加
        }
    }

    //根据title和parent_id查询一级分类
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject subject = subjectService.getOne(wrapper);
        return subject;

    }

    //根据title和parent_id查询二级分类
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoubject = subjectService.getOne(wrapper);
        return twoubject;
    }

    //读取表头内容
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //System.out.println("表头"+headMap);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
