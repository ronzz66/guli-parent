package com.ron.eduservice.entity.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data//excel的映射信息
public class SubjectData {
    @ExcelProperty(index = 0)
    private String oneSubjectName;


    @ExcelProperty(index = 1)
    private String twoSubjectName;

}
